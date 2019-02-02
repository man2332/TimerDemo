package com.example.timerdemo;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicHolder> {
    private List<Topic> topics = new ArrayList<>();

    @NonNull
    @Override
    public TopicAdapter.TopicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);

        return new TopicHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.TopicHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.topicName.setText(topic.getTopicName());
        holder.time.setText(topic.getTotalMin());//TODO: format this later
        holder.percentage.setText("1%");//TODO: get the daily time- then daily time / goal time
        holder.completionSign.setImageResource(R.drawable.ic_cancel);


    }

    @Override
    public int getItemCount() {
        return topics.size();
    }


    public class TopicHolder extends RecyclerView.ViewHolder{
        TextView topicName;
        TextView time;
        TextView percentage;
        ImageView completionSign;

        public TopicHolder(@NonNull View itemView) {
            super(itemView);

            topicName = itemView.findViewById(R.id.topicName_textView_item);
            time = itemView.findViewById(R.id.time_textView_item);
            percentage = itemView.findViewById(R.id.percentageDone_TextView_item);
            completionSign = itemView.findViewById(R.id.completion_imageView_item);
        }
    }

    public void setTopics(List<Topic> topics){
        this.topics = topics;
        notifyDataSetChanged();
    }
}
