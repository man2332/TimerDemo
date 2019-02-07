package com.example.timerdemo.utils;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.COMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.TIMERBROADCAST;

public class Utils {
    public static String convertMilisToTimeFormat(long millisUntilFinished) {
        String timeString = "";
        if (millisUntilFinished >= 3_600_000) {//if time is greater/equal than one hour
            timeString = String.format(Locale.getDefault(), "%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
        } else {
            timeString = String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
        }

        return timeString;
    }

    public static String convertMinToTimeFormat(String minutes) {
        int min = Integer.parseInt(minutes);
        String timeString = "";

        if (min >= 1440) {
            Log.d(TAG, "convertMinToTimeFormat: TOTAL MINS" + min);

            long hour = TimeUnit.MINUTES.toHours(min) - (TimeUnit.MINUTES.toDays(min) * 24);
            long minsFormat = TimeUnit.MINUTES.toMinutes(min) - (TimeUnit.MINUTES.toHours(min) * 60);

            String formatDay = TimeUnit.MINUTES.toDays(min) == 1 ? "%2d Day:" : TimeUnit.MINUTES.toDays(min) >= 10 ? "%02d Days:" : "%2d Days:";
            String formatHour = hour == 1? "%2dHr:" : hour >= 10 ? "%02dHrs:" : "%2dHrs:";
            String formatMin = minsFormat >= 10?"%02dMin" : "%2dMin";
            String formatTime = formatDay + formatHour + formatMin;
            timeString = String.format(Locale.getDefault(), formatTime,
                    TimeUnit.MINUTES.toDays(min),
                    TimeUnit.MINUTES.toHours(min) - (TimeUnit.MINUTES.toDays(min) * 24),
                    TimeUnit.MINUTES.toMinutes(min) - (TimeUnit.MINUTES.toHours(min) * 60));
        } else if (min >= 60) {
            String formatHour = TimeUnit.MINUTES.toHours(min) == 1? "%2dHr:" : TimeUnit.MINUTES.toHours(min) >= 10? "%02dHrs:" : "%2dHrs:";
            String formatMin = TimeUnit.MINUTES.toMinutes(min) >= 10 ? "%02dMin" : "%2dMin";
            String formatTime = formatHour + formatMin;
            timeString = String.format(Locale.getDefault(), formatTime,
                    TimeUnit.MINUTES.toHours(min),
                    TimeUnit.MINUTES.toMinutes(min) - (TimeUnit.MINUTES.toHours(min) * 60));
        } else {
            timeString = String.format(Locale.getDefault(), "%02d Minutes",
                    TimeUnit.MINUTES.toMinutes(min));
        }
        return timeString;
    }


//    private void registerLocalBroadcastReceivers(Context context) {
//        LocalBroadcastManager.getInstance(context).registerReceiver((timerReceiver),
//                new IntentFilter(TIMERBROADCAST));
//        LocalBroadcastManager.getInstance(context).registerReceiver((completedSuccessfullyReceiver),
//                new IntentFilter(COMPLETEDBROADCAST));
//    }

}
