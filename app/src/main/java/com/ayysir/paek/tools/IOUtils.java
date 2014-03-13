package com.ayysir.paek.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class IOUtils {
    public static String readOneLine(String sFile) {
        String sLine = null;
        if (fileExists(sFile)) {
            BufferedReader brBuffer;
            try {
                brBuffer = new BufferedReader(new FileReader(sFile), 512);
                try {
                    sLine = brBuffer.readLine();
                } finally {
                    brBuffer.close();
                }
            } catch (Exception e) {
                //logDebug("readOneLine: reading failed: " + e.getMessage());
                return readFileViaShell(sFile);
            }
        }
        return sLine;
    }

    /**
     * Fallback if everything fails
     *
     * @param filePath The file to read
     * @return The file's content
     */
    public static String readFileViaShell(String filePath) {
        return readFileViaShell(filePath, true);
    }

    public static String readFileViaShell(String filePath, boolean wholeFile) {
        try {
            List<String> mResult = Shell.SU.run("cat " + filePath);
            if (mResult != null) {
                if (mResult.size() != 0) {
                    if (wholeFile) {
                        String tmp = "";
                        for (String s : mResult) {
                            tmp += s + "\n";
                        }
                        return tmp;
                    } else {
                        return mResult.get(0);
                    }
                } else {
                    return "";
                }
            }
            return "";
        } catch (Exception exc) {
            return "";
        }
    }

    public static boolean writeValue(String filename, String value) {
        // the existence of the file is a requirement for the success ;)
        boolean success = fileExists(filename);
        if (success) {
            try {
                FileWriter fw = new FileWriter(filename);
                try {
                    fw.write(value);
                } finally {
                    fw.close();
                }
            } catch (IOException e) {
                //logDebug("writeValue: writing failed: " + e.getMessage());
                success = writeValueViaShell(filename, value);
            }
        }

        return success;
    }

    /**
     * Fallback if everything fails
     *
     * @param filename The file to write
     * @param value    The value to write
     */
    private static boolean writeValueViaShell(String filename, String value) {
        boolean success = false;
        List<String> tmpList = Shell.SU.run("busybox echo " + value + " > " + filename);

        // if we dont get any result, it means it works, else we got a "permission denied"
        // message and thus it didnt succeed
        if (tmpList != null) {
            if (tmpList.size() == 0) success = true;
        }

        return success;
    }

    public static boolean fileExists(String filename) {
        final boolean isExisting = new File(filename).exists();
        //logDebug("fileExists: " + filename + ": " + (isExisting ? "true" : "false"));
        return isExisting;
    }
}
