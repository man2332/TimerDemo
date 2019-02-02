package com.example.timerdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.OnClick;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.COMPLETEDBROADCAST;
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

    public static final TimerFragment newInstance(String title, String timerBroadcastType,
                                                  String completedBroadcastType)
    {
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

        current = 4;//4*5 = 20
        seekBar.setProgress(current - TIMEMIN);//start at 20 mins
        title.setText(getArguments().getString("title"));
        //for testing
        titleStr = title.getText().toString();
        Log.d(TAG, "onCreateView: "+titleStr);

        seekBar.setMax(TIMEMAX - TIMEMIN);
        timerTextView.setText(""+current*5);
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
        //*********************set the broadcast type for this fragment object
        timerBroadcastType =  getArguments().getString("timerBroadcastType");
        completedBroadcastType = getArguments().getString("completedBroadcastType");


        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.startStop_toggleButton_timer:
                if(!startStopToggleButtonTimer.isChecked()){
                    Log.d(TAG, "onViewClicked: start: "+titleStr);
                    seekBar.setVisibility(View.INVISIBLE);

                    Intent intent = new Intent(v.getContext(), TimerService.class);
                    intent.putExtra("timeInMins", parseInt(timerTextView.getText().toString()));
                    intent.putExtra("timerBroadcastType", timerBroadcastType);
                    intent.putExtra("completedBroadcastType", completedBroadcastType);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.getContext().startForegroundService(intent);
                    } else {
                        v.getContext().startService(intent);
                    }
                }else{
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
        Log.d(TAG, "onResume: TimeFragment: "+titleStr);
        super.onResume();
        registerLocalBroadcastReceivers(getContext());
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: TimeFragment: "+titleStr);
        unResisterBroadcastReceivers(getContext());
        super.onPause();
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
                //reset fragment_timer text
                timerTextView.setText("" + current);
                seekBar.setVisibility(View.VISIBLE);
                startStopToggleButtonTimer.setChecked(true);//check it again

                //TODO: add duration to the total_time_completed in the db but for now just show toast
                Log.d(TAG, "onReceive: duration: "+titleStr + duration);
            }
        };
        Log.d(TAG, "registerLocalBroadcastReceivers: "+titleStr+" : "+timerBroadcastType);

        LocalBroadcastManager.getInstance(context).registerReceiver((timerReceiver),
                new IntentFilter(timerBroadcastType));
        LocalBroadcastManager.getInstance(context).registerReceiver((completedSuccessfullyReceiver),
                new IntentFilter(completedBroadcastType));
    }
    public void unResisterBroadcastReceivers(Context context){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(timerReceiver);
        LocalBroadcastManager.getInstance(context).unregisterReceiver(completedSuccessfullyReceiver);

    }
}
