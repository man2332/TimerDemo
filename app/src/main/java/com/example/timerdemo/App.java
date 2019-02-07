package com.example.timerdemo;

import android.app.AlarmManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Objects;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.CHANNEL_ID;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: App.java");
        createNotificationChannel();
        setAlarm();
    }

    private void setAlarm() {
        Log.d(TAG, "setAlarm: SETING STARRT");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 6);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();
        if(cur.after(calendar)){
            Log.d(TAG, "setAlarm: ADDING A DAY");
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(getApplicationContext(), DailyReceiver.class);
        int ALARM1_ID = 10000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                ALARM1_ID,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getResources().getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_LOW);

            NotificationManager notificationManager = Objects.requireNonNull(
                    getSystemService(NotificationManager.class));

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
