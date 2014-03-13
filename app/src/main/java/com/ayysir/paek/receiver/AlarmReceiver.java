package com.ayysir.paek.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ayysir.paek.intentservices.UpdateCheckIntent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context mContext, Intent intent) {
        mContext.startService(new Intent(mContext, UpdateCheckIntent.class));
    }
}