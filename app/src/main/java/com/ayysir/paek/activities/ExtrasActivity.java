package com.ayysir.paek.activities;

/*
 * Copyright © 2014 ayysir
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ayysir.paek.R;
import com.ayysir.paek.tools.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.chainfire.libsuperuser.Shell;

public class ExtrasActivity extends Activity {

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File folder = new File(Environment.getExternalStorageDirectory() + "/PAEK/scripts");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        if (!success) {
        }

        setContentView(R.layout.extras_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        getActionBar().setHomeButtonEnabled(true);

        ListView mList = (ListView) findViewById(R.id.extras);

        mList.setOnItemClickListener(new LVItemClickListener());


        String[] strings = getResources().getStringArray(R.array.extras);

        // Prepare the header view for our list
        mList.setAdapter(new ArrayAdapter<String>(this, R.layout.extras_list_item,
                strings));


    }

    @SuppressLint("SdCardPath")
    private void selectItem(int position) {
        switch (position) {

            case 0:
                // fix permissions
                this.copyfixperms();
                Shell.SU.run("busybox mount -o remount,rw /system");
                if (new File("/sdcard/PAEK/scripts/fix_permissions").isFile()) {
                    Shell.SU.run("cp /sdcard/PAEK/scripts/fix_permissions /system/bin/fix_permissions");
                    Shell.SU.run("chmod a+x /system/bin/fix_permissions");


                    if (new File("/system/bin/fix_permissions").isFile()) {
                        Shell.SU.run("/system/bin/fix_permissions");
                        Toast toast = Toast.makeText(this, R.string.done, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else if (!new File("/system/bin/fix_permissions").isFile()) {
                        Toast toast = Toast.makeText(this, R.string.not_there, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                } else if (!new File("/sdcard/PAEK/scripts/fix_permissions").isFile()) {
                    Toast toast = Toast.makeText(this, R.string.not_sd_there, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
                break;

            case 1:
                // zip align
                this.copyzipalign();
                Shell.SU.run("busybox mount -o remount,rw /system");
                if (new File("/sdcard/PAEK/scripts/zip").isFile() && new File("/sdcard/PAEK/scripts/zipalign").isFile()) {
                    Shell.SU.run("cp /sdcard/PAEK/scripts/zip /system/bin/zip");
                    Shell.SU.run("cp /sdcard/PAEK/scripts/zipalign /system/bin/zipalign");
                    Shell.SU.run("chmod a+x /system/bin/zip");
                    Shell.SU.run("chmod a+x /system/bin/zipalign");

                    if (new File("/system/bin/zipalign").isFile()) {
                        Shell.SU.run("/system/bin/zipalign");
                        Toast toast = Toast.makeText(this, R.string.done, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else if (!new File("/system/bin/zipalign").isFile()) {
                        Toast toast = Toast.makeText(this, R.string.not_there, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                } else if (!new File("/sdcard/PAEK/scripts/zipalign").isFile()) {
                    Toast toast = Toast.makeText(this, R.string.not_sd_there, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                }
                break;

            case 2:
                // clean cache
                this.copyrmvcache();
                Shell.SU.run("busybox mount -o remount,rw /system");
                if (new File("/sdcard/PAEK/scripts/06removecache").isFile()) {
                    Shell.SU.run("cp /sdcard/PAEK/scripts/06removecache /system/bin/06removecache");
                    Shell.SU.run("cp /sdcard/PAEK/scripts/helpers.sh /system/etc/helpers.sh");
                    Shell.SU.run("chmod a+x /system/bin/06removecache");
                    Shell.SU.run("chmod a+x /system/bin/helpers.sh");


                    if (new File("/system/bin/06removecache").isFile()) {
                        Shell.SU.run("/system/bin/06removecache");
                        Toast toast = Toast.makeText(this, R.string.done, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    } else if (!new File("/system/bin/06removecache").isFile()) {
                        Toast toast = Toast.makeText(this, R.string.not_there, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.show();
                    }
                } else if (!new File("/sdcard/PAEK/scripts/06removecache").isFile()) {
                    Toast toast = Toast.makeText(this, R.string.not_sd_there, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.show();
                }

                break;

            case 3:
                //copy assets
                this.copyzipalign();
                this.copyfixperms();
                this.copyrmvcache();

                // copy to init.d
                Shell.SU.run("cp /sdcard/PAEK/scripts/zip /system/etc/init.d/zip");
                Shell.SU.run("cp /sdcard/PAEK/scripts/zipalign /system/etc/init.d/zipalign");
                Shell.SU.run("cp /sdcard/PAEK/scripts/fix_permissions /system/etc/init.d/fix_permissions");
                Shell.SU.run("cp /sdcard/PAEK/scripts/06removecache /system/etc/init.d/06removecache");
                Shell.SU.run("cp /sdcard/PAEK/scripts/helpers.sh /system/etc/helpers.sh");


                // make executable
                Shell.SU.run("chmod a+x /system/etc/init.d/fix_permissions");
                Shell.SU.run("chmod a+x /system/etc/init.d/06removecache");
                Shell.SU.run("chmod a+x /system/etc/init.d/zip");
                Shell.SU.run("chmod a+x /system/etc/init.d/zipalign");
                Shell.SU.run("chmod a+x /system/etc/init.d/helpers.sh");

                break;

            case 4:
                //delete all
                Shell.SU.run("rm /system/bin/fix_permissions");
                Shell.SU.run("rm /system/bin/zip");
                Shell.SU.run("rm /system/bin/zipalign");
                Shell.SU.run("rm /system/bin/06removecache");
                Shell.SU.run("rm /system/bin/helpers.sh");
                Shell.SU.run("rm /system/etc/init.d/zipalign");
                Shell.SU.run("rm /system/etc/init.d/zip");
                Shell.SU.run("rm /system/etc/init.d/06removecache");
                Shell.SU.run("rm /system/etc/init.d/fix_permissions");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void copyrmvcache() {
        copyFileOrDir("06removecache"); // copy all files in assets folder in my project
        copyFileOrDir("helpers.sh");
        Shell.SU.run("mv /sdcard/PAEKhelpers.sh /sdcard/PAEK/scripts/helpers.sh");
        Shell.SU.run("mv /sdcard/PAEK06removecache /sdcard/PAEK/scripts/06removecache");
    }

    private void copyfixperms() {
        copyFileOrDir("fix_permissions"); // copy all files in assets folder in my project
        Shell.SU.run("mv /sdcard/PAEKfix_permissions.sh /sdcard/PAEK/scripts/fix_permissions");
    }

    private void copyzipalign() {
        copyFileOrDir("zip"); // copy all files in assets folder in my project
        copyFileOrDir("zipalign");
        Shell.SU.run("mv /sdcard/PAEKzip /sdcard/PAEK/scripts/zip");
        Shell.SU.run("mv /sdcard/PAEKzipalign /sdcard/PAEK/scripts/zipalign");
    }

    private void copyFileOrDir(String path) {
        AssetManager assetManager = this.getAssets();
        String assets[];
        try {
            Log.i(Constants.LOG_TAG, "copyFileOrDir() " + path);
            assets = assetManager.list(path);
            if (assets.length == 0) {
                copyFile(path);
            } else {
                String fullPath = Constants.TARGET_BASE_PATH + path;
                Log.i(Constants.LOG_TAG, "path=" + fullPath);
                File dir = new File(fullPath);
                if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                    Log.i(Constants.LOG_TAG, "could not create dir " + fullPath);
                for (String asset : assets) {
                    String p;
                    if (path.equals(""))
                        p = "";
                    else
                        p = path + "/";

                    if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
                        copyFileOrDir(p + asset);
                }
            }
        } catch (IOException ex) {
            Log.e(Constants.LOG_TAG, "I/O Exception", ex);
        }
    }

    private void copyFile(String filename) {
        AssetManager assetManager = this.getAssets();

        InputStream in;
        OutputStream out;
        String newFileName = null;
        try {
            Log.i(Constants.LOG_TAG, "copyFile() " + filename);
            in = assetManager.open(filename);
            if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
                newFileName = Constants.TARGET_BASE_PATH + filename.substring(0, filename.length() - 4);
            else
                newFileName = Constants.TARGET_BASE_PATH + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Exception in copyFile() of " + newFileName);
            Log.e(Constants.LOG_TAG, "Exception in copyFile() " + e.toString());
        }
    }

    @SuppressLint("SdCardPath")
    private class LVItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);

            view.isHorizontalFadingEdgeEnabled();
        }
    }
}

