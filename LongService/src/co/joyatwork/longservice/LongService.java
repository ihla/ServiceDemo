package co.joyatwork.longservice;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.util.Log;


public class LongService extends Service {
	private static final int TIMER_TASK_SCHEDULE_RATE_MILLIS = 60 * 1000;
	private static final String TAG = "LongService";
	private static final String CSV_HEADER_LOG_FILE = "Time,Event";
    private static final char CSV_DELIM = ',';
	private MyTimerTasks myTimerTask;
	private PrintWriter logWriter;
	private WakeLock wakeLock;
	
	
	private static class MyTimerTasks extends TimerTask {
		
		public AtomicBoolean run = new AtomicBoolean();
		private WeakReference<LongService> serviceRef;

		public MyTimerTasks(WeakReference<LongService> serviceRef) {
			this.serviceRef = serviceRef;
			
		}

		@Override
		public void run() {
			if (run.get() == false) {
				return;
			}
			Log.d(TAG, "MyTimerTask time: " + SystemClock.uptimeMillis());
			
			LongService s = serviceRef.get();
			if (s != null) {
				s.writeLog("running");
			}
		}
		
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate()");
		
        File logFile = new File(getExternalCacheDir(), "log.csv");
		try {

			//FileWriter calls directly OS for every write request
			//For better performance the FileWriter is wrapped into BufferedWriter, which calls out for a batch of bytes
			//PrintWriter allows a human-readable writing into a file
			logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
			logWriter.println(CSV_HEADER_LOG_FILE);


		} catch (IOException e) {
			Log.e(TAG, "Could not open CSV file(s)", e);
		}

		PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
	    wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LongService");
	    wakeLock.acquire();
	    
		myTimerTask = new MyTimerTasks(new WeakReference<LongService>(this));
		myTimerTask.run.set(true);
		Timer t = new Timer();
		t.scheduleAtFixedRate(myTimerTask, 0, TIMER_TASK_SCHEDULE_RATE_MILLIS);
		
		writeLog("onCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand()");
		writeLog("onStartCommand");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
		myTimerTask.run.set(false);
		writeLog("onDestroy");
		wakeLock.release();
		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		Log.d(TAG, "onLowMemory()");
		writeLog("onLowMemory");
		super.onLowMemory();
	}

	@SuppressLint("NewApi")
	@Override
	public void onTrimMemory(int level) {
		Log.d(TAG, "onTrimMemory() level: " + level);
		writeLog("onTrimMemory level: " + level);
		super.onTrimMemory(level);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void writeLog(String event) {
		if (logWriter != null) {
			StringBuffer sb = new StringBuffer()
				.append(SystemClock.uptimeMillis()).append(CSV_DELIM)
				.append(event)
				;

			logWriter.println(sb.toString());
			if (logWriter.checkError()) {
				Log.w(TAG, "Error writing log data");
			}
		}
	}

}
