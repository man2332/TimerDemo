package com.example.timerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.List;

public class TopicListActivity extends AppCompatActivity {
    private TopicViewModel topicViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_list);
        //-set up recyclerview obj
//        RecyclerView recyclerView = findViewById(R.id.recyclerView_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        final TopicAdapter topicAdapter = new TopicAdapter();
//        recyclerView.setAdapter(topicAdapter);
//
//        //-setting up the viewmodel & live data to work with "this" activity
//        topicViewModel = ViewModelProviders.of(this).get(TopicViewModel.class);
//        topicViewModel.getAllTopics().observe(this, new Observer<List<Topic>>() {
//            @Override
//            public void onChanged(List<Topic> topics) {
//                topicAdapter.setTopics(topics);
//            }
//        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_topic_list_container,
                new TopicListFragment()).commit();
    }
}