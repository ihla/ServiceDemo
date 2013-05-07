package co.joyatwork.servicedemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onDestroy");
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onPause");
		super.onPause();
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
    	//Log.d(TAG, "onRestart");
		super.onRestart();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
    	Log.d(TAG, "onResume");
		super.onResume();
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
    	//Log.d(TAG, "onStart");
		super.onStart();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
    	//Log.d(TAG, "onStop");
		super.onStop();
	}


	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        
        Button startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startService(new Intent(MainActivity.this, DemoService.class)); // explicitly
			}
		});
        
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopService(new Intent(MainActivity.this, DemoService.class)); // explicitly
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
