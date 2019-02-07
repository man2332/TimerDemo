package com.example.timerdemo;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "topic_table")
public class Topic {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "topic_name")
    String topicName;
    @ColumnInfo(name = "total_min")
    String totalMin;
//    @ColumnInfo(name = "goal_min")
//    String goalMin;


    //two variables below combine together for providing a history of successful goals completed
    //TODO: add a completion(check mark) to dates that user completed the goal time successfully
//    Date date;
//    Boolean didComplete;


    public Topic(String topicName, String totalMin) {
        this.topicName = topicName;
        this.totalMin = totalMin;
//        this.goalMin = goalMin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

//    public String getGoalMin() {
//        return goalMin;
//    }

//    public void setGoalMin(String goalHours) {
//        this.goalMin = goalHours;
//    }

    public String getTotalMin() {
        return totalMin;
    }
    public void setTotalMin(String totalMin){
        this.totalMin = totalMin;
    }
}

//TABLE_TOPIC
//TABLE_TOPIC_HISTORY