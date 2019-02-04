package com.example.timerdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.timerdemo.TimerActivity.TAG;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_GOAL;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_ID;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_NAME;
import static com.example.timerdemo.utils.Constants.ADDEDIT_EXTRA_TOPIC_TOTAL_MIN;
import static com.example.timerdemo.utils.Constants.DELETE_EXTRA_TOPIC;

public class TopicListFragment extends Fragment {

    private TopicViewModel topicViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_topic_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        //-set up recyclerview obj
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));

        final TopicAdapter topicAdapter = new TopicAdapter();
        recyclerView.setAdapter(topicAdapter);

        //-setting up the viewmodel & live data to work with "this" activity
        topicViewModel = ViewModelProviders.of(getActivity()).get(TopicViewModel.class);
        topicViewModel.getAllTopics().observe(this, new Observer<List<Topic>>() {
            @Override
            public void onChanged(List<Topic> topics) {
                topicAdapter.setTopics(topics);//here is where the adapter gets it's data
            }

        });
        //TODO:MOVE this delete and click functionality into TopicListTimersFragment
        Bundle bundle = getArguments();
        int id = bundle != null ? bundle.getInt(DELETE_EXTRA_TOPIC, -1) : -1;
        if(bundle != null || id != -1){
            Log.d(TAG, "onViewCreated: TOPICLIST DELETED!!!");
            Topic deleteThisTopic = topicViewModel.getAllTopics().getValue().get(id -1);
            topicViewModel.delete(deleteThisTopic);
        }

        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab_add_topic_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                        new TopicListAddEditFragment()).commit();
            }
        });
        //attach a ItemTouchHelper to a RecyclerView
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        }).attachToRecyclerView(recyclerView);

        //**implement onclick listener - when a user clicks on an item - send them to the TopicListAddEditFragment
        topicAdapter.setOnTopicItemClickListener(new TopicAdapter.OnTopicItemClickListener() {
            @Override
            public void onTopicItemClick(Topic topic) {
                Bundle bundle = new Bundle();
                bundle.putInt(ADDEDIT_EXTRA_TOPIC_ID, topic.getId());
                bundle.putString(ADDEDIT_EXTRA_TOPIC_NAME,topic.getTopicName());
                bundle.putString(ADDEDIT_EXTRA_TOPIC_TOTAL_MIN,topic.getTotalMin());
                bundle.putString(ADDEDIT_EXTRA_TOPIC_GOAL, topic.getGoalMin());
                Fragment fragment = new TopicListAddEditFragment();
                fragment.setArguments(bundle);

                //to to new frag
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                        fragment).commit();

            }
        });

    }
}
