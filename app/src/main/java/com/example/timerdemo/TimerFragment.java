package com.example.timerdemo;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.OnClick;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.COMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.POMOTIMERBROADCAST;
import static com.example.timerdemo.utils.Constants.SHAREDPREFS;
import static com.example.timerdemo.utils.Constants.TIMEMAX;
import static com.example.timerdemo.utils.Constants.TIMEMIN;
import static com.example.timerdemo.utils.Constants.TIMERBROADCAST;
import static java.lang.Integer.parseInt;

public class TimerFragment extends Fragment implements View.OnClickListener {

    BroadcastReceiver timerReceiver;
    BroadcastReceiver completedSuccessfullyReceiver;
    private String timerBroadcastType;
    private String completedBroadcastType;

    private String currentTime;

    TextView timerTextView;
    SeekBar seekBar;
    int current;
    ToggleButton startStopToggleButtonTimer;

    //for testing
    String titleStr;

//    PowerManager.WakeLock wakeLock;
//    PowerManager mgr;

    public static final TimerFragment newInstance(String title, String timerBroadcastType,
                                                  String completedBroadcastType) {
        TimerFragment timerFragment = new TimerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("timerBroadcastType", timerBroadcastType);
        bundle.putString("completedBroadcastType", completedBroadcastType);
        timerFragment.setArguments(bundle);
        return timerFragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_timer, container, false);
        TextView title = v.findViewById(R.id.title_textView_fragment_timer);
        seekBar = v.findViewById(R.id.seekbar_fragment_timer);
        timerTextView = v.findViewById(R.id.timer_textView_fragment_timer);
        startStopToggleButtonTimer = v.findViewById(R.id.startStop_toggleButton_timer);



        startStopToggleButtonTimer.setOnClickListener(this);

        title.setText(getArguments().getString("title"));
        titleStr = title.getText().toString();
        Log.d(TAG, "onCreateView: " + titleStr);
        
        //seekBar position starts at 0

        //-currentPomo,Short, Long helps keep track of the last set time in each timer for when user returns
        SharedPreferences prefs = getActivity().getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        int prefCurrentPomo = prefs.getInt("currentPomo", -1);
        int prefCurrentShort = prefs.getInt("currentShort", -1);
        int prefCurrentLong = prefs.getInt("currentLong", -1);
        //this switch determines what the current seekbar position will be
        switch (titleStr){
            case "Pomodoro":
                if (prefCurrentPomo != -1) {
                    current = prefCurrentPomo;
                    current /= 5;
                }else{
                    current = 4;
                }
                break;
            case "Short Break":
                if (prefCurrentShort != -1) {
                    current = prefCurrentShort;
                    current /= 5;
                }else{
                    current = 4;
                }
                break;
            case "Long Break":
                if (prefCurrentLong != -1) {
                    current = prefCurrentLong;
                    current /= 5;
                }else{
                    current = 4;
                }
                break;
            default:
                Log.d(TAG, "onCreateView: TIMERFRAGMENT SWITCH ERROR");
                current = 4;
        }
        
        
        

        seekBar.setProgress(current - TIMEMIN);//start the seekBar[3] position which is 20 mins

        //for testing
        

        seekBar.setMax(TIMEMAX - TIMEMIN);//set max to 23, which means it has 24 positions since we count 0
        current *= 5;//now current is no longer position in our seekBar but the value lolz, * by 5 to get the value of the bar
        timerTextView.setText("" + current);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                current = progress + TIMEMIN;
                current *= 5;
                timerTextView.setText("" + current);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //*********************get the broadcast type for this fragment object
        //-originally setArguments() when instantiating the objects
        timerBroadcastType = getArguments().getString("timerBroadcastType");
        completedBroadcastType = getArguments().getString("completedBroadcastType");


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startStop_toggleButton_timer:
                if (!startStopToggleButtonTimer.isChecked()) {
                    Log.d(TAG, "onViewClicked: start: " + titleStr + " : "+timerBroadcastType);
                    seekBar.setVisibility(View.INVISIBLE);

                    Intent intentService = new Intent(v.getContext(), TimerService.class);
                    intentService.putExtra("timeInMins", parseInt(timerTextView.getText().toString()));
                    intentService.putExtra("timerBroadcastType", timerBroadcastType);
                    intentService.putExtra("completedBroadcastType", completedBroadcastType);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Toast.makeText(getActivity().getApplicationContext(), ">= O", Toast.LENGTH_SHORT).show();
                        v.getContext().startForegroundService(intentService);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "< O", Toast.LENGTH_SHORT).show();
                        v.getContext().startService(intentService);
                    }
                } else {
                    Log.d(TAG, "onViewClicked: stop : " + current);
                    Intent intent = new Intent(v.getContext(), TimerService.class);
                    v.getContext().stopService(intent);
                    timerTextView.setText("" + current);
                    seekBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onResume() {
        Log.d(TAG, "onResume: TimeFragment: " + titleStr);
        super.onResume();
//        stopTimer();
        registerLocalBroadcastReceivers(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: TimeFragment: " + titleStr);
        //if screen is on, then stop the timer & unregister
        if(((PowerManager) getActivity().getSystemService(Context.POWER_SERVICE)).isScreenOn()){
            stopTimer();
            unResisterBroadcastReceivers(getContext());
        }



        SharedPreferences prefs = getActivity().getSharedPreferences(SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        switch (titleStr) {
            case "Pomodoro":
                editor.putInt("currentPomo", current);
                editor.apply();
                break;
            case "Short Break":
                editor.putInt("currentShort", current);
                editor.apply();
                break;
            case "Long Break":
                editor.putInt("currentLong", current);
                editor.apply();
            default:
                Log.d(TAG, "onPause: TIMERFRAGMENT: ERROR NO titleStr");
                break;
                    
        }
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: TIMERFRAGMENT");
        super.onSaveInstanceState(outState);
        //put seekbar position when leaving screen or screen config
//        outState.putInt("current", current);
    }

    private void registerLocalBroadcastReceivers(Context context) {
        timerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras() != null) {
                    currentTime = intent.getExtras().getString("timeLeft");
                    timerTextView.setText("" + currentTime);
                }
            }
        };

        completedSuccessfullyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Long duration = intent.getExtras().getLong("duration");
                String timerBroadcastType = intent.getExtras().getString("timerBroadcastType");
                //reset fragment_timer text
                timerTextView.setText("" + current);
                seekBar.setVisibility(View.VISIBLE);
                startStopToggleButtonTimer.setChecked(true);//check it again

                if(timerBroadcastType.equals(POMOTIMERBROADCAST)) {//if a pomo timer finished, add up time
                    //TODO: add duration to the total_time_completed in the db but for now just show toast
                    Log.d(TAG, "onReceive: duration: " + titleStr + duration);
                    Log.d(TAG, "onReceive: ADDING TIME");
                    long timeInSeconds = duration / 1000;
                    long timeInMin = duration / 60_000;
                    //TODO:it stores timeInSeconds but it should be timeInMins- fix later
                    if (timeInMin >= 5) {//min store time is 5 mins
                        ((TimerActivity) getActivity()).updateTime(timeInMin);//testing*****************************************************************************
                    }
                }else{
                    Log.d(TAG, "onReceive: TIMERFRAGMENT NOT ADDING TIME");
                }
            }
        };
        Log.d(TAG, "registerLocalBroadcastReceivers: " + titleStr + " : " + timerBroadcastType);

        LocalBroadcastManager.getInstance(context).registerReceiver((timerReceiver),
                new IntentFilter(timerBroadcastType));
        LocalBroadcastManager.getInstance(context).registerReceiver((completedSuccessfullyReceiver),
                new IntentFilter(completedBroadcastType));
    }

    public void unResisterBroadcastReceivers(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(timerReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(completedSuccessfullyReceiver);

    }

    //stop service and set the button to "Start"
    public void stopTimer() {
        Log.d(TAG, "stopTimer: TIMERFRAGMENT: STOP DA TIMER");
//        if(null != startStopToggleButtonTimer && timerTextView != null && seekBar != null &&
//        getActivity() != null) {

            startStopToggleButtonTimer.setChecked(true);
            timerTextView.setText("" + current);
            seekBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(getActivity().getApplicationContext(), TimerService.class);
            getActivity().stopService(intent);
//        }
//        Log.d(TAG, "stopTimer: TIMERFRAGMENT ERRRRORR NO FIND");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        wakeLock.release();
    }
}
