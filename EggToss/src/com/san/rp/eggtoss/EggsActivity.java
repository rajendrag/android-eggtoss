package com.san.rp.eggtoss;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class EggsActivity extends Activity {

	 /** Called when the activity is first created. */
	
		private static final String TAG = EggsActivity.class.getSimpleName();
		private MainGamePanel gamePanel;
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        // requesting to turn the title OFF
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        // making it full screen
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        // set our MainGamePanel as the View
	        gamePanel = new MainGamePanel(this);
	        setContentView(gamePanel);
	        Log.d(TAG, "View added");
	    }

		@Override
		protected void onDestroy() {
			Log.d(TAG, "Destroying...");
			gamePanel.stopThread();
			super.onDestroy();
		}

		@Override
		protected void onStop() {
			Log.d(TAG, "Stopping...");
			gamePanel.stopThread();
			super.onStop();
			System.gc();
		}
		
		protected void onPause() {
			Log.d(TAG, "Pausing...");
			gamePanel.stopThread();
			super.onPause();
		}
		
		protected void onResume()
	    {
	        super.onResume();
	        Log.d(TAG, "onResume");
	        if (gamePanel.isSurfaceCreated())
	        {
	            gamePanel.createGameThread();
	        }
	    }

}
