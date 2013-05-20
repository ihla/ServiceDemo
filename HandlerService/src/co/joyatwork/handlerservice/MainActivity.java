package co.joyatwork.handlerservice;

import co.joyatwork.handlerservice.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";

	private final class AccelerometerUpdateReciever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "AccelerometerUpdateReciever.onReceive " + intent.getAction());
			
			if (intent.hasExtra(getResources().getString(R.string.acc_x_value))) {
				TextView xValueTextView = (TextView) findViewById(id.accXValTextView);
				xValueTextView.setText(
						intent.getExtras().getCharSequence(getResources().getString(R.string.acc_x_value)));
			}
			if (intent.hasExtra(getResources().getString(R.string.acc_y_value))) {
				TextView xValueTextView = (TextView) findViewById(id.accYValTextView);
				xValueTextView.setText(
						intent.getExtras().getCharSequence(getResources().getString(R.string.acc_y_value)));
			}
			if (intent.hasExtra(getResources().getString(R.string.acc_z_value))) {
				TextView xValueTextView = (TextView) findViewById(id.accZValTextView);
				xValueTextView.setText(
						intent.getExtras().getCharSequence(getResources().getString(R.string.acc_z_value)));
			}
			
		}
		
	}
	private BroadcastReceiver accelerometerUpdateReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Button quitButton = (Button) findViewById(R.id.quitButton);
		quitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// stop service implicitly
				Intent intent = new Intent(MainActivity.this, HelperService.class);
				stopService(intent);
				cancelNotification();
				finish();
			}

		});
		
		accelerometerUpdateReceiver = new AccelerometerUpdateReciever();
		
		// start service explicitly
		
		Intent intent = new Intent(MainActivity.this, HelperService.class);
		startService(intent);
		launchNotification();
		
	}

	private void launchNotification() {
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.notification_icon)
		.setContentTitle("Handler Service")
		.setContentText("press to launch");
	
		Intent launcActivity = new Intent(this, MainActivity.class);
		
		TaskStackBuilder backStackBuilder = TaskStackBuilder.create(this);
		backStackBuilder.addParentStack(MainActivity.class);
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

	@Override
	protected void onResume() {
		super.onResume();
		
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
		lbm.registerReceiver(accelerometerUpdateReceiver, 
				new IntentFilter(getResources().getString(R.string.acc_update_action)));
	}

	@Override
	protected void onPause() {
		super.onPause();

		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
		lbm.unregisterReceiver(accelerometerUpdateReceiver);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
