package com.example.robotnavigator;



import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;


public class RobotNavigatorActivity extends Activity {
	
	private RobotNavigatorService mService;
	
	private boolean mResumed;

	 private ServiceConnection mConnection = new ServiceConnection() {
	       

	        @Override
	        public void onServiceDisconnected(ComponentName name) {
	            // Do nothing.
	        }

			@Override
			public void onServiceConnected(ComponentName service, IBinder binderService) {
				 if (binderService instanceof RobotNavigatorService) {
		                mService = (RobotNavigatorService) binderService;
		                openOptionsMenu();
		            }
				
			}
	    };
	    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(this, RobotNavigatorService.class), mConnection, 0);
       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	
        getMenuInflater().inflate(R.menu.robot_navigator, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;
        openOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
    }

    @Override
    public void openOptionsMenu() {
        if (mResumed && mService != null) {
            super.openOptionsMenu();
        }
    }

  

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.floor_plan:
               //show floor_plan
                return true;
            case R.id.direct_control:
                //show direct control 
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onOptionsMenuClosed(Menu menu) {
        super.onOptionsMenuClosed(menu);

        unbindService(mConnection);

        // We must call finish() from this method to ensure that the activity ends either when an
        // item is selected from the menu or when the menu is dismissed by swiping down.
        finish();
    }
    
   
    
}
