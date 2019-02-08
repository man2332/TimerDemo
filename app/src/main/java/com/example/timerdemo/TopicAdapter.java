package com.example.timerdemo;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timerdemo.utils.ItemTouchHelperAdapter;
import com.example.timerdemo.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.timerdemo.TimerActivity.TAG;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicHolder> implements ItemTouchHelperAdapter {
    private List<Topic> topics = new ArrayList<>();
    private OnTopicItemClickListener listener;
    private Context context;

    public TopicAdapter(Context context) {
        this.context = context;
    }

    //TODO: set up clickable ViewHolders - user can click a view to go to a timer
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
//        holder.time.setText(topic.getTotalMin());//TODO: format this later
        int screenSize = Utils.screenSize(context);
        holder.time.setText(Utils.convertMinToTimeFormat(topic.getTotalMin(), screenSize));
        //holder.percentage.setText("1%");//TODO: get the daily time- then daily time / goal time
        holder.playImage.setImageResource(R.drawable.ic_play_arrow);


    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    //ItemToucherHelper implementation - when user drags and drops items
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG, "onItemMove: TOPICADAPTER.java: SWITCHING");
        if(fromPosition < toPosition){
            for (int i = fromPosition; i < toPosition; i++){
                Collections.swap(topics, i, i+ 1);
            }
        }else{
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(topics, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition,toPosition);
        return true;
    }


    public class TopicHolder extends RecyclerView.ViewHolder{
        TextView topicName;
        TextView time;
        //TextView percentage;
        ImageView playImage;

        public TopicHolder(@NonNull View itemView) {
            super(itemView);

            topicName = itemView.findViewById(R.id.topicName_textView_item);
            time = itemView.findViewById(R.id.time_textView_item);
            //percentage = itemView.findViewById(R.id.percentageDone_TextView_item);
            playImage = itemView.findViewById(R.id.play_imageView_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onTopicItemClick(topics.get(position), position);//return the topic, and the index position of that topic
                    }
                }
            });

            playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();//get the position of the item that was clicked
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        //listener can be null if it's not implemented/assigned, NO_POSITION is a constant for -1
                        listener.onPlayItemClick(topics.get(position));
                    }

                }
            });
        }
    }

    public void setTopics(List<Topic> topics){
        Log.d(TAG, "setTopics: TopicAdapter: ");
        this.topics = topics;
        notifyDataSetChanged();
    }
    //-this shows how to pass data from an adapter to another class
    public Topic getTopicAtPosition(int position){
        return topics.get(position);
        //u can later use the Topic obj returned to delete it or do whatever u want with it
    }


    public interface OnTopicItemClickListener{
        void onTopicItemClick(Topic topic, int position);
        void onPlayItemClick(Topic topic);
        //-u can pass whatever u want to this method, play it it anyway u want
        //-u can pass position, etc
    }
    public void setOnTopicItemClickListener(OnTopicItemClickListener listener){
        this.listener = listener;
    }
}
