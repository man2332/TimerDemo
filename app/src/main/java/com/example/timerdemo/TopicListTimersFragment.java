package com.example.timerdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.DAILYBROADCAST;
import static com.example.timerdemo.utils.Constants.SHAREDPREFS_DAILY_TIME;

public class TopicListTimersFragment extends Fragment {
    private TopicViewModel topicViewModel;




    @BindView(R.id.studied_time_textView_list)
    TextView dailyTimeTextView;

    //@BindView(R.id.fab_add_topic_list)
    FloatingActionButton floatingActionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list_timers, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: TopicListTimersFragment.java-registerReceiver DAILYBROADCAST");

        IntentFilter filter = new IntentFilter(DAILYBROADCAST);
        getActivity().registerReceiver(refreshScreenReceiver,filter);

        floatingActionButton = getActivity().findViewById(R.id.fab_add_topic_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DailyReceiver.class);
                getActivity().sendBroadcast(intent);
            }
        });

        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));

        TopicAdapter topicAdapter = new TopicAdapter();
        recyclerView.setAdapter(topicAdapter);

        Observer<List<Topic>> topicObserver = new Observer<List<Topic>>() {
            @Override
            public void onChanged(List<Topic> topics) {
                topicAdapter.setTopics(topics);
            }
        };
        topicViewModel = ViewModelProviders.of(getActivity()).get(TopicViewModel.class);
        topicViewModel.getAllTopics().observe(this, topicObserver);


        TopicAdapter.OnTopicItemClickListener listener = new TopicAdapter.OnTopicItemClickListener() {
            @Override
            public void onTopicItemClick(Topic topic) {
                //opens a TimerActivity
                Toast.makeText(getContext(), topic.getTopicName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), TimerActivity.class);
                intent.putExtra("title", topic.getTopicName());
                intent.putExtra("id", topic.getId());

                startActivity(intent);


            }
        };
        topicAdapter.setOnTopicItemClickListener(listener);

        setAlarm();
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS_DAILY_TIME, Context.MODE_PRIVATE);
        int dailyTime = sharedPreferences.getInt("dailyTime", 0);
        Log.d(TAG, "onViewCreated: TimersFrag: " + dailyTime);
        dailyTimeTextView.setText(String.valueOf(dailyTime));


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(refreshScreenReceiver != null){
            getActivity().unregisterReceiver(refreshScreenReceiver);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void setAlarm() {
        Log.d(TAG, "setAlarm: SETING STARRT");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 39);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();
        if(cur.after(calendar)){
            Log.d(TAG, "setAlarm: ");
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(getActivity().getApplicationContext(), DailyReceiver.class);
        int ALARM1_ID = 10000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(),
                ALARM1_ID,intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private BroadcastReceiver refreshScreenReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: TOPICLISTTIMER REFRESHED ");
            Toast.makeText(context, "TOPIC LIST REFRESHED", Toast.LENGTH_SHORT).show();
            SharedPreferences prefs = getActivity().getSharedPreferences(SHAREDPREFS_DAILY_TIME, Context.MODE_PRIVATE);
            int time = prefs.getInt("dailyTime",1);
            dailyTimeTextView.setText(""+time);

        }
    };




}
