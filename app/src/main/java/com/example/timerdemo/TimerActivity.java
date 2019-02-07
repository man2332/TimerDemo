package com.example.timerdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;

import static com.example.timerdemo.utils.Constants.LONGCOMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.LONGTIMERBROADCAST;
import static com.example.timerdemo.utils.Constants.POMOCOMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.POMOTIMERBROADCAST;
import static com.example.timerdemo.utils.Constants.SHAREDPREFS_DAILY_TIME;
import static com.example.timerdemo.utils.Constants.SHORTCOMPLETEDBROADCAST;
import static com.example.timerdemo.utils.Constants.SHORTTIMERBROADCAST;
import static java.lang.Integer.parseInt;

public class TimerActivity extends AppCompatActivity {
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


    TopicViewModel topicViewModel;
    private int id;
    private String topicName;
    private String goal;

    private int dailyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        setTitle(intent.getStringExtra("title"));
        id = intent.getIntExtra("id", -1);
        //************************FRAGMENT VIEW PAGER***********************************************
        List<Fragment> fragmentList = getFragments();
        timerPagerAdapter = new TimerPagerAdapter(getSupportFragmentManager(), fragmentList);
        ViewPager viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(timerPagerAdapter);

        topicViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);

        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS_DAILY_TIME, MODE_PRIVATE);
        dailyTime = sharedPreferences.getInt("dailyTime",0);
        Log.d(TAG, "onCreate: TimerActivity: dailyTime: "+dailyTime);

        //-to display the x icon top left corner
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home://user clicks x icon- top left- to close timer activity
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();

        fragments.add(TimerFragment.newInstance("Pomodoro",POMOTIMERBROADCAST, POMOCOMPLETEDBROADCAST));
        fragments.add(TimerFragment.newInstance("Short Break", SHORTTIMERBROADCAST, SHORTCOMPLETEDBROADCAST));
        fragments.add(TimerFragment.newInstance("Long Break", LONGTIMERBROADCAST, LONGCOMPLETEDBROADCAST));

        return fragments;
    }
    public void updateTime(long timeInMins){


//        Topic topic = topicViewModel.getAllTopics().getValue().get(id-1);
        Topic topic = topicViewModel.getTopicById(id);
        long topicOriginalTime = Integer.parseInt(topic.getTotalMin());
        String sumTime = String.valueOf(topicOriginalTime += timeInMins);
        topic.setTotalMin(sumTime);
        topicViewModel.update(topic);

        //update the dailyTime
//        Integer originalDailyTime = topicViewModel.getDailyTime().getValue();
//        Integer time = Integer.valueOf((int) timeInMins);
//        topicViewModel.getDailyTime().setValue(originalDailyTime += time);

        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS_DAILY_TIME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int dailyTime = sharedPreferences.getInt("dailyTime", 0);
        editor.putInt("dailyTime", dailyTime+=timeInMins);
        editor.commit();
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
