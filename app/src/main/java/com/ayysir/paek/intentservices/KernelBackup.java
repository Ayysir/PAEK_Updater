package com.ayysir.paek.intentservices;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.ayysir.paek.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import eu.chainfire.libsuperuser.Shell;

public class KernelBackup extends IntentService {

    private static final String TAG = "PAEK";

    public KernelBackup() {
        super("KernelBackupIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (!new File(Environment.getExternalStorageDirectory() + "/PAEK/backup", "boot.img").delete())
            Log.v(TAG, "Couldn't delete boot.img.");

        Shell.SU.run("dd if=/dev/block/platform/msm_sdcc.1/by-name/system of=" + Environment.getExternalStorageDirectory().getPath() + "/PAEK/backup/boot.img");

        if (new File(Environment.getExternalStorageDirectory() + "/PAEK/backup", "kernel.zip").delete())
            Log.v(TAG, "Couldn't delete kernel.zip");

        try {

            InputStream mInputStream = getResources().openRawResource(R.raw.d801_kernel);
            OutputStream mOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/PAEK/backup", "kernel.zip"));
            byte[] buffer = new byte[1024];
            int length;

            try {

                while ((length = mInputStream.read(buffer, 0, buffer.length)) != -1)
                    mOutputStream.write(buffer, 0, length);

            } finally {
                mInputStream.close();
                mOutputStream.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error writing empty kernel zip!");
        }

        try {

            File zipFile = new File(Environment.getExternalStorageDirectory() + "/PAEK/backup", "kernel.zip");
            File[] file = {new File(Environment.getExternalStorageDirectory() + "/PAEK/backup", "boot.img")};

            addFilesToExistingZip(zipFile, file);

        } catch (IOException e) {
            Log.e(TAG, "Error inserting kernel into zip!");
        }
    }

    private void addFilesToExistingZip(File zipFile, File[] files) throws IOException {
        File tempFile = new File(Environment.getExternalStorageDirectory() + "/PAEK/backup", "temp_kernel.zip");
        if (!tempFile.delete())
            Log.v(TAG, "Couldn't delete temp_kernel.zip.");

        boolean renameOk = zipFile.renameTo(tempFile);
        if (!renameOk) {
            throw new RuntimeException("could not rename " + zipFile.getAbsolutePath() + " to " + tempFile.getAbsolutePath());
        }
        byte[] buf = new byte[1024];

        ZipInputStream mZipInputStream = new ZipInputStream(new FileInputStream(tempFile));
        ZipOutputStream mZipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));

        ZipEntry entry = mZipInputStream.getNextEntry();
        while (entry != null) {
            String name = entry.getName();
            boolean notInFiles = true;
            for (File f : files) {
                if (f.getName().equals(name)) {
                    notInFiles = false;
                    break;
                }
            }
            if (notInFiles) {

                mZipOutputStream.putNextEntry(new ZipEntry(name));

                int len;
                while ((len = mZipInputStream.read(buf)) > 0)
                    mZipOutputStream.write(buf, 0, len);

            }
            entry = mZipInputStream.getNextEntry();
        }
        mZipInputStream.close();

        for (File i : files) {
            InputStream in = new FileInputStream(i);
            mZipOutputStream.putNextEntry(new ZipEntry(i.getName()));

            int len;
            while ((len = in.read(buf)) > 0)
                mZipOutputStream.write(buf, 0, len);

            mZipOutputStream.closeEntry();
            in.close();
        }

        mZipOutputStream.close();
        if (!tempFile.delete())
            Log.v(TAG, "Couldn't delete temp_kernel.zip.");
    }
}
