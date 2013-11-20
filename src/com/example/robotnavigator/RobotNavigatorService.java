package com.example.robotnavigator;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

public class RobotNavigatorService extends Service {
	
	private static final String TAG = RobotNavigatorService.class.getSimpleName();
    private static final String LIVE_CARD_ID = "robotnavigator";

    private NavigatorDrawHandler mCallback;

    private TimelineManager mTimelineManager;
    private LiveCard mLiveCard;

    @Override
    public void onCreate() {
        super.onCreate();
        mTimelineManager = TimelineManager.from(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            Log.d(TAG, "Publishing LiveCard");
            mLiveCard = mTimelineManager.getLiveCard(LIVE_CARD_ID);

            // Keep track of the callback to remove it before unpublishing.
            mCallback = new NavigatorDrawHandler(this);
            mLiveCard.enableDirectRendering(true).getSurfaceHolder().addCallback(mCallback);
            mLiveCard.setNonSilent(true);

            Intent menuIntent = new Intent(this, RobotNavigatorActivity.class);
            mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));

            mLiveCard.publish();
            Log.d(TAG, "Done publishing LiveCard");
        } else {
            
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            Log.d(TAG, "Unpublishing LiveCard");
            if (mCallback != null) {
                mLiveCard.getSurfaceHolder().removeCallback(mCallback);
            }
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }

}
