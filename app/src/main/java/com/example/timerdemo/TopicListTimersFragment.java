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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;
import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_INDEX_POSITION;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_ID;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_NAME;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_TOTAL_MIN;
import static com.example.timerdemo.utils.Constants.DAILYBROADCAST;
import static com.example.timerdemo.utils.Constants.DELETE_EXTRA_TOPIC;
import static com.example.timerdemo.utils.Constants.SHAREDPREFS_DAILY_TIME;
import static com.example.timerdemo.utils.Constants.STUDIEDTODAYTEXT;

public class TopicListTimersFragment extends Fragment {
    private TopicViewModel topicViewModel;

    private Fragment topicListTimersFragment;


    @BindView(R.id.studied_today_textView_list)
    TextView dailyTimeTextView;

    //@BindView(R.id.fab_add_topic_list)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.date_textView_list)
    TextView dateTextView;

    TopicAdapter topicAdapter;

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
        getActivity().setTitle("List of Timers");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        IntentFilter filter = new IntentFilter(DAILYBROADCAST);
        getActivity().registerReceiver(refreshScreenReceiver,filter);

//        floatingActionButton = getActivity().findViewById(R.id.fab_add_topic_list);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), DailyReceiver.class);
//                getActivity().sendBroadcast(intent);
//            }
//        });
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab_add_topic_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                        new TopicListAddEditDeleteFragment()).commit();
            }
        });


        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));

        topicAdapter = new TopicAdapter(getActivity().getApplicationContext());
        recyclerView.setAdapter(topicAdapter);
        //DRAGING AND DROPING ITEMS
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(customItemTouchHelper());
        itemTouchHelper.attachToRecyclerView(recyclerView);



        Observer<List<Topic>> topicObserver = new Observer<List<Topic>>() {
            @Override
            public void onChanged(List<Topic> topics) {
                topicAdapter.setTopics(topics);
            }
        };
        topicViewModel = ViewModelProviders.of(getActivity()).get(TopicViewModel.class);
        topicViewModel.getAllTopics().observe(this, topicObserver);

        //when user clicks on play button or clicks on the item view itself to edit it
        TopicAdapter.OnTopicItemClickListener listener = new TopicAdapter.OnTopicItemClickListener() {
            @Override
            public void onTopicItemClick(Topic topic, int position) {
                Log.d(TAG, "onTopicItemClick: TopicListTimersFragment.java: ON ITEM CLICKED");
                Bundle bundle = new Bundle();
                bundle.putInt(ADDEDIT_EXTRA_TOPIC_ID, topic.getId());
                bundle.putInt(ADDEDIT_EXTRA_TOPIC_INDEX_POSITION, position);
                bundle.putString(ADDEDIT_EXTRA_TOPIC_NAME,topic.getTopicName());
                bundle.putString(ADDEDIT_EXTRA_TOPIC_TOTAL_MIN,topic.getTotalMin());
//                bundle.putString(ADDEDIT_EXTRA_TOPIC_GOAL, topic.getGoalMin());
                Fragment fragment = new TopicListAddEditDeleteFragment();
                fragment.setArguments(bundle);

                //to to new frag
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                        fragment).addToBackStack(null).commit();
            }
            @Override
            public void onPlayItemClick(Topic topic) {
                //opens a TimerActivity
                Toast.makeText(getContext(), topic.getTopicName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), TimerActivity.class);
                intent.putExtra("title", topic.getTopicName());
                intent.putExtra("id", topic.getId());

                startActivity(intent);
            }
        };
        topicAdapter.setOnTopicItemClickListener(listener);

        //setAlarm();
        //check if user just pressed delete from the last fragment and came from there
        Bundle bundle = getArguments();
        int id = bundle != null ? bundle.getInt(DELETE_EXTRA_TOPIC, -1) : -1;
        if(bundle != null || id != -1){
            int indexPosition = bundle.getInt(ADDEDIT_EXTRA_TOPIC_INDEX_POSITION);
            Log.d(TAG, "onViewCreated: TOPICLIST DELETED at POSITION: "+indexPosition);
//            Topic deleteThisTopic = topicAdapter.getTopicAtPosition(indexPosition);
//            Topic deleteThisTopic = topicViewModel.getAllTopics().getValue().get(id -1);
            Topic deleteThisTopic = topicViewModel.getAllTopics().getValue().get(indexPosition-1);//get() - get the topic using index value
            topicViewModel.delete(deleteThisTopic);
        }
        //-format and set up the date text
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM-d-yyyy");
        String formattedDate = df.format(c);
        dateTextView.setText(formattedDate);


    }
    //for drag and drop
    private ItemTouchHelper.Callback customItemTouchHelper() {
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ACTION_STATE_DRAG,ItemTouchHelper.UP | ItemTouchHelper.DOWN);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                topicAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
        return callback;
    };

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHAREDPREFS_DAILY_TIME, Context.MODE_PRIVATE);
        int dailyTime = sharedPreferences.getInt("dailyTime", 0);
        Log.d(TAG, "onViewCreated: TimersFrag: " + dailyTime);
        String studiedTodayText = STUDIEDTODAYTEXT.concat(String.valueOf(dailyTime));
        dailyTimeTextView.setText(String.valueOf(studiedTodayText));


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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public void setAlarm() {
        Log.d(TAG, "setAlarm: SETING STARRT");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 32);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();
        if(cur.after(calendar)){
            Log.d(TAG, "setAlarm: ADDING A DAY");
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
            dailyTimeTextView.setText(STUDIEDTODAYTEXT+time);

        }
    };




}
