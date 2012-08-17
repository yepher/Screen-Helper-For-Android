package com.yepher.screenhelper;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.PowerManager;
import android.util.Log;

public class ScreenHelper extends Application {
	private static final String TAG = ScreenHelper.class.getSimpleName();
	
	private PowerManager.WakeLock wakeLock;
	
	private boolean isActive = true;
	
	@Override
    public void onCreate() {
        super.onCreate();

        if (PowerUtil.isConnected(this)) {
        	disAllowAutoScreenTimeout();
        } else {
        	allowAutoScreenTimeout();
        }
        
        BroadcastReceiver receiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                if (plugged == BatteryManager.BATTERY_PLUGGED_AC) {
                    disAllowAutoScreenTimeout();
                    
                } else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) {
                	disAllowAutoScreenTimeout();
                	
                } else if (plugged == 0) {
                    allowAutoScreenTimeout();
                    
                } else {
                    Log.e(TAG, "Not sure what state the device is in. Will leave in current state.");
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver, filter);
        
    }
	
	public void onTerminate() {
		allowAutoScreenTimeout();
	}
	
	public void disAllowAutoScreenTimeout() {
		if (isActive == false) {
			return;
		}
		
		if (wakeLock == null) {
			Log.e(TAG, "Will dis-allow screen timeout.");
			 CharSequence title = "Screen timeout";
             CharSequence detail = "Disabled";
             CharSequence message = "Screen timeout - Disabled";
             notify(title, detail, message);
			
			PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Screen Helper");
			wakeLock.acquire();
		}
	}
	
	public void allowAutoScreenTimeout() {
		if (wakeLock != null) {
			Log.e(TAG, "Will allow screen timeout.");
			
			CharSequence title = "Screen timeout";
            CharSequence detail = "Enabled";
            CharSequence message = "Screen timeout - Enabled";
            notify(title, detail, message);
            
			wakeLock.release();
			wakeLock = null;
		}
	}
	
	private void notify(CharSequence title, CharSequence detail, CharSequence message) {

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		int icon = android.R.drawable.star_big_off;
		if (wakeLock == null) {
		    icon = android.R.drawable.star_big_on;
		}
		
		Notification notification = new Notification(icon, message, 10000L);
		
		Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
 
		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(getApplicationContext(), title, detail, pendingIntent);
		notificationManager.notify(0, notification);
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
		
		if (isActive == false) {
			allowAutoScreenTimeout();
		} else if (PowerUtil.isConnected(this)) {
        	disAllowAutoScreenTimeout();
        } 
	}
	
}
