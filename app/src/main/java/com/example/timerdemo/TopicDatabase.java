package com.example.timerdemo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Topic.class}, version = 1, exportSchema = false)
public abstract class TopicDatabase extends RoomDatabase {
    //-ROOM does the magic of defining this method for u, to return a TopicDao with defined methods
    public abstract TopicDao topicDao();
    //-instance is the one and only db instance that will be created
    public static TopicDatabase instance;
    //-this method only can run one at a time, no matter how many times its called
    public static synchronized TopicDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TopicDatabase.class, "topic_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)//call when method before .build()
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private TopicDao topicDao;

        private PopulateDbAsyncTask(TopicDatabase db){
            topicDao = db.topicDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //populate db with fake data when user installs app
//            topicDao.insert((new Topic("Math","210")));
//            topicDao.insert((new Topic("Java","30")));
//            topicDao.insert((new Topic("English","150")));
            return null;
        }
    }
}
