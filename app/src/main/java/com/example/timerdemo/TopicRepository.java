package com.example.timerdemo;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TopicRepository {
    private TopicDao topicDao;
    private LiveData<List<Topic>> allTopics;

    public TopicRepository(Application application) {
        TopicDatabase database = TopicDatabase.getInstance(application);
        topicDao = database.topicDao();
        allTopics = topicDao.getAllTopics();
    }

    public LiveData<List<Topic>> getAllTopics(){ return allTopics;}
    //******************************INSERTING INTO DB USING DAO*************************************
    //-insert a topic into the db
    //-1 arg(the topic to be inserted)
    public void insert(Topic topic){
        new InsertTopicAsyncTask(topicDao).execute(topic);
    }
    public class InsertTopicAsyncTask extends AsyncTask<Topic, Void, Void>{

        private TopicDao topicDao;

        public InsertTopicAsyncTask(TopicDao topicDao){
            this.topicDao = topicDao;
        }

        @Override
        protected Void doInBackground(Topic... topics) {
            topicDao.insert(topics[0]);
            return null;
        }
    }
    //*************************UPDATING AN ENTRY IN DB USING DAO************************************
    public void update(Topic topic){ new UpdateTopicAsyncTask(topicDao).execute(topic);}
    public class UpdateTopicAsyncTask extends AsyncTask<Topic, Void, Void>{

        private TopicDao topicDao;
        public UpdateTopicAsyncTask(TopicDao topicDao){
            this.topicDao = topicDao;
        }

        @Override
        protected Void doInBackground(Topic... topics) {
            topicDao.update(topics[0]);
            return null;
        }
    }
    //************************DELETING AN ENTRY DB USING DAO****************************************
    public void delete(Topic topic){ new DeleteTopicAsyncTask(topicDao).execute(topic);}
    public class DeleteTopicAsyncTask extends AsyncTask<Topic, Void, Void>{

        private TopicDao topicDao;
        public DeleteTopicAsyncTask(TopicDao topicDao){
            this.topicDao = topicDao;
        }

        @Override
        protected Void doInBackground(Topic... topics) {
            topicDao.delete(topics[0]);
            return null;
        }
    }
    //*****************************DELETING ALL DB USING DAO****************************************
    public void deleteAllTopics(){ new DeleteAllTopicsAsyncTask(topicDao).execute();}
    public class DeleteAllTopicsAsyncTask extends AsyncTask<Void, Void, Void>{

        private TopicDao topicDao;
        public DeleteAllTopicsAsyncTask(TopicDao topicDao){
            this.topicDao = topicDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            topicDao.deleteAllTopics();
            return null;
        }
    }

}
