package com.yepher.screenhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "scrHelper_" + NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: " + intent);
        
        Bundle extras = intent.getExtras();
        if (extras != null) {

        }
    }
}