package com.yepher.screenhelper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.disableMonitorButton);
		toggleButton.setChecked(false);
		final ScreenHelper screenHelper = (ScreenHelper) getApplication();
		screenHelper.setActive(!toggleButton.isChecked());

		toggleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d(TAG, "Button Pressed: State: " + toggleButton.isChecked());
				screenHelper.setActive(!toggleButton.isChecked());
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

}
