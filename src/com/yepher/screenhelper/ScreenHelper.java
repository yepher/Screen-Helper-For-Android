package com.yepher.screenhelper;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.util.Log;

public class ScreenHelper extends Application {
	private static final String TAG = ScreenHelper.class.getSimpleName();
	
	PowerManager.WakeLock wakeLock;
	
	@Override
    public void onCreate() {
        super.onCreate();

        if (PowerUtil.isConnected(this)) {
        	disAllpwAutoScreenTimeout();
        } else {
        	allowAutoScreenTimeout();
        }
        
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                    disAllpwAutoScreenTimeout();
                } else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                	disAllpwAutoScreenTimeout();
                } else if (plugged == 0) {
                    allowAutoScreenTimeout();
                } else {
                    Log.e(TAG, "Now sure what state the batter device is in. Will leave in current state.");
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
        
    }
	
	public void onTerminate() {
		allowAutoScreenTimeout();
	}
	
	private void disAllpwAutoScreenTimeout() {
		Log.e(TAG, "Will dis-allow screen timeout.");
		if (wakeLock == null) {
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Screen Helper");
			wakeLock.acquire();
		}
	}
	
	private void allowAutoScreenTimeout() {
		Log.e(TAG, "Will allow screen timeout.");
		if (wakeLock != null) {
			wakeLock.release();
			wakeLock = null;
		}
	}
	
}
