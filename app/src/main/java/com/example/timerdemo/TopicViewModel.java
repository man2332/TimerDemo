package com.example.timerdemo;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TopicViewModel extends AndroidViewModel {
    private TopicRepository topicRepository;
    private LiveData<List<Topic>> allTopics;

    public TopicViewModel(@NonNull Application application) {
        super(application);
        topicRepository = new TopicRepository(application);
        allTopics = topicRepository.getAllTopics();
    }
    public LiveData<List<Topic>> getAllTopics(){
        return allTopics;
    }

    public void insert(Topic topic){
        topicRepository.insert(topic);
    }
    public void update(Topic topic){
        topicRepository.update(topic);
    }
    public void delete(Topic topic){
        topicRepository.delete(topic);
    }
    public void deleteAll(){
        topicRepository.deleteAllTopics();
    }
}
//the system calls this ctor, we never instantiate a ViewModel object, the system takes care of that
//we just call ViewModelProviders. The system keeps track if there is a viewmodel obj already created
//and calls the ctor only if it's not. If we created a ViewModel obj with "new", we would just
//create a new instance of it each time "new" is called
// , rather than using only one viewmodel instance to keep track of all the data
//ViewModelProviders will keep track of only one viewmodel obj for us to use