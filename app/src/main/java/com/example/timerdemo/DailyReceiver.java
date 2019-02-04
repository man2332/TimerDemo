package com.example.timerdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.DAILYBROADCAST;
import static com.example.timerdemo.utils.Constants.SHAREDPREFS_DAILY_TIME;

public class DailyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREDPREFS_DAILY_TIME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("dailyTime", 0);
        editor.commit();
        Log.d(TAG, "onReceive: DailyReceiver.java - setting sharedprefs - dailyTime to 0");
        Log.d(TAG, "onReceive: DailyReceiver.java - sending Custom broadcast - DAILYBROADCAST");
        Toast.makeText(context, "RECEIVED !!!", Toast.LENGTH_LONG).show();

        //send a receiver to the TopicListTimersFragment to refresh
        Intent refreshIntent = new Intent(DAILYBROADCAST);
        context.sendBroadcast(refreshIntent);
    }
}
