package com.example.timerdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.timerdemo.utils.Constants.COMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.LONGCOMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.LONGTIMERBROADCAST;
import static com.example.timerdemo.utils.Constants.POMOCOMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.POMOTIMERBROADCAST;
import static com.example.timerdemo.utils.Constants.SHORTCOMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.SHORTTIMERBROADCAST;
import static com.example.timerdemo.utils.Constants.TIMEMAX;
import static com.example.timerdemo.utils.Constants.TIMEMIN;
import static com.example.timerdemo.utils.Constants.TIMERBROADCAST;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "ttag";


//    @BindView(R.id.timer_textView_main)
//    TextView timerTextViewMain;
//    @BindView(R.id.startStop_toggleButton_main)
//    ToggleButton startStopToggleButtonMain;

    //BroadcastReceiver timerReceiver;
    //BroadcastReceiver completedSuccessfullyReceiver;
    //@BindView(R.id.seekBar)
    //SeekBar seekBar;

    //private String currentTime;
    //private int current;

    TimerPagerAdapter timerPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //******************************************************************************************
        //**********************************RECIEVER DEFINTIONS*************************************
//        timerReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getExtras() != null) {
//                    currentTime = intent.getExtras().getString("timeLeft");
//                    timerTextViewMain.setText("" + currentTime);
//                }
//            }
//        };
//
//        completedSuccessfullyReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Long duration = intent.getExtras().getLong("duration");
//                //reset fragment_timer text
//                timerTextViewMain.setText("" + current * 5);
//                seekBar.setVisibility(View.VISIBLE);
//
//                //TODO: add duration to the total_time_completed in the db but for now just show toast
//                Log.d(TAG, "onReceive: duration: " + duration);
//            }
//        };
        //************************SEEKBAR TIMER*****************************************************
//        seekBar.setMax(TIMEMAX - TIMEMIN);
//        current = 4;//4*5 = 20
//        seekBar.setProgress(current - TIMEMIN);//start at 20 mins
//        timerTextViewMain.setText("" + current * 5);
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                current = progress + TIMEMIN;
//                current *= 5;
//                timerTextViewMain.setText("" + current);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        //************************FRAGMENT VIEW PAGER***********************************************
        List<Fragment> fragmentList = getFragments();
        timerPagerAdapter = new TimerPagerAdapter(getSupportFragmentManager(), fragmentList);
        ViewPager viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(timerPagerAdapter);
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TimerFragment.newInstance("Pomodoro",POMOTIMERBROADCAST, POMOCOMPLETEDBROADCAST));
        fragments.add(TimerFragment.newInstance("Short Break", SHORTTIMERBROADCAST, SHORTCOMPLETEDBROADCAST));
        fragments.add(TimerFragment.newInstance("Long Break", LONGTIMERBROADCAST, LONGCOMPLETEDBROADCAST));

        return fragments;
    }


//    @OnClick(R.id.startStop_toggleButton_main)
//    public void onViewClicked() {
//        //registerLocalBroadcastReceivers();
//
//        //-User clicks start button
//        if (!startStopToggleButtonMain.isChecked()) {//stop = checked, play = unchecked
//            Log.d(TAG, "onViewClicked: start");
//            seekBar.setVisibility(View.INVISIBLE);
//
//            Intent intent = new Intent(getApplicationContext(), TimerService.class);
//            intent.putExtra("timeInMins", parseInt(timerTextViewMain.getText().toString()));
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                getApplicationContext().startForegroundService(intent);
//            } else {
//                getApplicationContext().startService(intent);
//            }
//        } else {
//            //-user clicks stop button
//            //stop the running service
//            Log.d(TAG, "onViewClicked: stop : " + current);
//            Intent intent = new Intent(getApplicationContext(), TimerService.class);
//            stopService(intent);
//            timerTextViewMain.setText("" + current);
//            seekBar.setVisibility(View.VISIBLE);
//        }
//    }

//    private void registerLocalBroadcastReceivers() {
//        LocalBroadcastManager.getInstance(this).registerReceiver((timerReceiver),
//                new IntentFilter(TIMERBROADCAST));
//        LocalBroadcastManager.getInstance(this).registerReceiver((completedSuccessfullyReceiver),
//                new IntentFilter(COMPLETEDBROADCAST));
//    }


}
