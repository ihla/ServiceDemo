package co.joyatwork.longservice;

import co.joyatwork.longservice.R.id;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {
	
	private static final String TAG = "Main";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button quitButton = (Button) findViewById(id.quitButton);
		quitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, LongService.class);
				stopService(intent);
				cancelNotification();
				finish();
			}
		});
		
		Log.d(TAG, "onCreate()");
	}

	@Override
	protected void onResume() {
		super.onResume();

		Log.d(TAG, "onResume()");

		Intent intent = new Intent(Main.this, LongService.class);
		startService(intent);
		launchNotification();
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "onPause()");

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.d(TAG, "onLowMemory()");
	}

	@SuppressLint("NewApi")
	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		Log.d(TAG, "onTrimMemory() level: " + level);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void launchNotification() {
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.notification_icon)
		.setContentTitle("Long Service")
		.setContentText("press to launch");
	
		Intent launcActivity = new Intent(this, Main.class);
		
		TaskStackBuilder backStackBuilder = TaskStackBuilder.create(this);
		backStackBuilder.addParentStack(Main.class);
		backStackBuilder.addNextIntent(launcActivity);
		PendingIntent launchPendingActivity = backStackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		notificationBuilder.setContentIntent(launchPendingActivity);
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notificationBuilder.build());
	}

	private void cancelNotification() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}


}
