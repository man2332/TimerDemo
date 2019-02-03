package com.example.timerdemo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
@Dao
public interface TopicDao {

    @Insert
    void insert(Topic topic);
    @Update
    void update(Topic topic);
    @Delete
    void delete(Topic topic);

    @Query("DELETE FROM topic_table")//remove this method later-just for testing purposes
    void deleteAllTopics();
    @Query("SELECT * FROM topic_table")
    LiveData<List<Topic>> getAllTopics();

    @Query("SELECT * FROM topic_table WHERE id LIKE :search")
    Topic getTopicById(String search);

}
