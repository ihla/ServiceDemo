package co.joyatwork.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class DemoService extends Service {
	
	private static final String TAG = "DemoService";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreate()");
		super.onCreate();
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

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
