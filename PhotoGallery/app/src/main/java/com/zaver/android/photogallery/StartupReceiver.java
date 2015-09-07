package com.zaver.android.photogallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Zaver on 9/7/15.
 */
public class StartupReceiver extends BroadcastReceiver {

    private static final String TAG ="StartupReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());

        boolean isAlarmOn = QueryPreferences.isAlarmOn(context);
        PollService.setServiceAlarm(context, isAlarmOn);
    }
}
