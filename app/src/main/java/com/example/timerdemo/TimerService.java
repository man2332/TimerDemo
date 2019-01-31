package com.example.timerdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.example.timerdemo.utils.Utils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.timerdemo.utils.Constants.CHANNEL_ID;
import static com.example.timerdemo.utils.Constants.TIMERBROADCAST;

public class TimerService extends Service {

    private CountDownTimer countDownTimer;
    LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long mTimeLeftInMillis = 60_000;//default timer is 1 min


        if(countDownTimer == null){
            countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String timeLeft = Utils.convertMilisToTimeFormat(millisUntilFinished);
                    localBroadcastManager.sendBroadcast(new Intent(TIMERBROADCAST)
                                                        .putExtra("timeLeft", timeLeft));

                }

                @Override
                public void onFinish() {
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                    }
                }
            }.start();
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Title")
                .setContentText("Example Text")
                .setSmallIcon(R.drawable.ic_attach_money_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);


        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
