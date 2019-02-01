package com.example.timerdemo.utils;

import android.content.Context;
import android.content.IntentFilter;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
        }else{
            timeString = String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
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
