package com.example.timerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class TopicListTimersFragment extends Fragment {
    private TopicViewModel topicViewModel;

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
                Toast.makeText(getContext(), topic.getTopicName(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), TimerActivity.class);
                intent.putExtra("title",topic.getTopicName());
                intent.putExtra("id", topic.getId());

                startActivity(intent);

            }
        };
        topicAdapter.setOnTopicItemClickListener(listener);
    }


}
