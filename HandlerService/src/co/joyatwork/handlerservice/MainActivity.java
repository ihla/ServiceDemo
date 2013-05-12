package co.joyatwork.handlerservice;

import co.joyatwork.handlerservice.R.id;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
			
			if (intent.hasExtra("co.joyatwork.handleservice.X_VALUE")) {
				TextView xValueTextView = (TextView) findViewById(id.accXValTextView);
				xValueTextView.setText(intent.getExtras().getCharSequence("co.joyatwork.handleservice.X_VALUE"));
			}
			if (intent.hasExtra("co.joyatwork.handleservice.Y_VALUE")) {
				TextView xValueTextView = (TextView) findViewById(id.accYValTextView);
				xValueTextView.setText(intent.getExtras().getCharSequence("co.joyatwork.handleservice.Y_VALUE"));
			}
			if (intent.hasExtra("co.joyatwork.handleservice.Z_VALUE")) {
				TextView xValueTextView = (TextView) findViewById(id.accZValTextView);
				xValueTextView.setText(intent.getExtras().getCharSequence("co.joyatwork.handleservice.Z_VALUE"));
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
				finish();
			}
		});
		
		accelerometerUpdateReceiver = new AccelerometerUpdateReciever();
		
		// start service explicitly
		Intent intent = new Intent(MainActivity.this, HelperService.class);
		startService(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
		lbm.registerReceiver(accelerometerUpdateReceiver, new IntentFilter("co.joyatwork.handlerservice.ACC_UPDATE"));
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
