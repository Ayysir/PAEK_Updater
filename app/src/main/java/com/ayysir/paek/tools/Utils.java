/*
 * Copyright © 2014 Andre "ayysir" Saddler
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

package com.ayysir.paek.tools;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ayysir.paek.MainActivity;
import com.ayysir.paek.R;
import com.ayysir.paek.receiver.AlarmReceiver;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import eu.chainfire.libsuperuser.Shell;


public class Utils {

    public static final int NOTIFICATION_ID = 1236855265;
    public static final String G2 = "http://91.hostingsharedbox.com:3000/g2k/";
    private static final int ALARM_ID = 698524;
    public static JSONObject SERVER_JSON = null;

    //Check for supported devices via build.prop
    public static String getDevice() {
        try {
            Process mProcess = Runtime.getRuntime().exec("getprop " + "ro.product.device");
            BufferedReader mBufferedReader = new BufferedReader(new InputStreamReader(
                    mProcess.getInputStream()));
            StringBuilder mLog = new StringBuilder();
            String line;
            while ((line = mBufferedReader.readLine()) != null) mLog.append(line);
            return mLog.toString();
        } catch (IOException e) {
            Log.v("Runtime Error: ", e.toString());
        }
        return null;
    }

    // check if PEAK is installed
    public static boolean isPAEKinstalled() {
        StringBuilder mStringBuilder = new StringBuilder(System.getProperty("os.version")); // os version = Kernel version (about phone screen)
        return (mStringBuilder.indexOf("PAEK") != -1); // check if os.version contains "PEAK" | This will return true or false
    }

    // get installed PAEK version
    public static Float getSystemPAEKVersion() {
        StringBuilder mStringBuilder = new StringBuilder(System.getProperty("os.version")); // os version = Kernel version (about phone screen)
        mStringBuilder.delete(0, mStringBuilder.lastIndexOf("v") + 1);
        return Float.parseFloat(mStringBuilder.toString());
    }

    // get version number from server [name] string
    public static float getServerVersion(String mVersion) {
        StringBuilder mStringBuilder = new StringBuilder(mVersion);
        mStringBuilder.delete(0, mStringBuilder.indexOf("v") + 1);
        mStringBuilder.delete(mStringBuilder.indexOf("-"), mStringBuilder.length());
        return Float.parseFloat(mStringBuilder.toString());
    }

    //TODO: look into making a dictionary for whitelisting devices
    //check if device is supported
    public static boolean isDeviceSuppported(Context mContext) {
        if (!Shell.SU.available() && !Arrays.asList(Constants.SUPPORTED_DEVICES).contains(getDevice())) {

            //SET TOAST GRAVITY AT TOP OF APP
            Toast toast = Toast.makeText(mContext, mContext.getResources().getString(R.string.unsupported_device), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            ((MainActivity) mContext).finish();

        }
        return (Shell.SU.available() && Arrays.asList(Constants.SUPPORTED_DEVICES).contains(getDevice()));
    }

    public static boolean isG2Suppported(Context mContext) {
        if (!Shell.SU.available() && !Arrays.asList(Constants.G2_VARIANTS).contains(getDevice())) {
            //SET TOAST GRAVITY AT TOP OF APP
            Toast toast = Toast.makeText(mContext, mContext.getResources().getString(R.string.unsupported_device), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            ((MainActivity) mContext).finish();
        }
        return (Shell.SU.available() && Arrays.asList(Constants.G2_VARIANTS).contains(getDevice()));
    }

    public static boolean isN4Suppported(Context mContext) {
        if (!Shell.SU.available() && !Arrays.asList(Constants.NEXUS).contains(getDevice())) {
            //SET TOAST GRAVITY AT TOP OF APP
            Toast toast = Toast.makeText(mContext, mContext.getResources().getString(R.string.unsupported_device), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            ((MainActivity) mContext).finish();
        }
        return (Shell.SU.available() && Arrays.asList(Constants.NEXUS).contains(getDevice()));
    }

    //check if device is connected to mobile network
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // check if device is connected to mobile network
    public static boolean isOnWifi(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    public static void createNotification(Context mContext, String mContentText) {
        Intent mIntent = new Intent(mContext, MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(mContext, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mNotificationBuilder = new Notification.Builder(mContext)
                .setContentTitle(mContext.getString(R.string.app_name))
                .setContentText(mContentText)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(mPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(Utils.NOTIFICATION_ID, mNotificationBuilder.build());
    }

    public static void dismissNotification(Context mContext) {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public static void setAlarm(Context mContext) {
        Intent mIntent = new Intent(mContext, AlarmReceiver.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.cancel(mPendingIntent);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, mPendingIntent);
    }
}
