package com.example.timerdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.timerdemo.utils.Constants.TIMERBROADCAST;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.timer_textView_main)
    TextView timerTextViewMain;
    @BindView(R.id.startStop_toggleButton_main)
    ToggleButton startStopToggleButtonMain;

    BroadcastReceiver timerReceiver;

    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        timerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                    if(intent.getExtras() != null){
                        currentTime = intent.getExtras().getString("timeLeft");
                        timerTextViewMain.setText(currentTime);
                    }
            }
        };

    }

    @OnClick(R.id.startStop_toggleButton_main)
    public void onViewClicked() {
        registerLocalBroadcastReceivers();

        Intent intent = new Intent(getApplicationContext(), TimerService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            getApplicationContext().startForegroundService(intent);
        }else{
            getApplicationContext().startService(intent);
        }

    }

    private void registerLocalBroadcastReceivers(){
        LocalBroadcastManager.getInstance(this).registerReceiver((timerReceiver),
                new IntentFilter(TIMERBROADCAST));
    }
}
