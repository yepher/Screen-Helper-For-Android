package com.yepher.screenhelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PostBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent serviceIntent = new Intent("com.yepher.screenhelper.MainActivity");
            context.startService(serviceIntent);
        }
    }
}