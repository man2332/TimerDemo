package com.example.timerdemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.example.timerdemo.utils.Utils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.CHANNEL_ID;
import static com.example.timerdemo.utils.Constants.COMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.TIMERBROADCAST;

public class TimerService extends Service {

    private CountDownTimer countDownTimer;
    LocalBroadcastManager localBroadcastManager;

    private String timerBroadcastType;
    private String completedBroadcastType;

    @Override
    public void onCreate() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int mTimeLeftInMins = intent.getExtras().getInt("timeInMins",0);
        timerBroadcastType = intent.getExtras().getString("timerBroadcastType");
        completedBroadcastType = intent.getExtras().getString("completedBroadcastType");
        Log.d(TAG, "onStartCommand: "+timerBroadcastType);

        long mTimeLeftInMillis = mTimeLeftInMins * 1_000;//1 min is 60k milliseconds
//        long mTimeLeftInMillis = mTimeLeftInMins * 43_200_000;//each second is 12 hours

        if(countDownTimer == null){
            countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String timeLeft = Utils.convertMilisToTimeFormat(millisUntilFinished);
                    localBroadcastManager.sendBroadcast(new Intent(timerBroadcastType)
                            .putExtra("timeLeft", timeLeft));

                }

                @Override
                public void onFinish() {
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                        //send broadcast here to main that fragment_timer completed successfully
                        localBroadcastManager.sendBroadcast(new Intent(completedBroadcastType)//.putExtra("duration", 2880));
                                .putExtra("duration", mTimeLeftInMillis));//each second will be 12 hours hehe
                    }
                    stopSelf();
                }
            }.start();
        }

        Intent notificationIntent = new Intent(this, TimerActivity.class);
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

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: TIMERSERVICE "+timerBroadcastType);
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
        super.onDestroy();
    }
}
