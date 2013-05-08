package co.joyatwork.servicedemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;


public class DemoService extends Service {
	
	private static final String TAG = "DemoService";

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.notification_icon)
			.setContentTitle("Demo Service")
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

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand(" 
					+ (intent != null ? intent.toString() : "null") 
					+ ", " 
					+ flags 
					+ ", " 
					+ startId 
					+ ")");
		
		Toast.makeText(this, "service starting " + startId, Toast.LENGTH_SHORT).show();
		
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");

		Toast.makeText(this, "service stoping", Toast.LENGTH_SHORT).show();

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
