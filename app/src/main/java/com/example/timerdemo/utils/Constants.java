package com.example.timerdemo.utils;

public class Constants {
    public static final String CHANNEL_ID = "TimerDemoChannel";

    public static final String TIMERBROADCAST = "com.example.broadcast";
    public static final String COMPLETEDBROADCAST = "com.example.completed.broadcast";

    public static final String POMOTIMERBROADCAST = "com.example.broadcast.pomo";
    public static final String SHORTTIMERBROADCAST = "com.example.broadcast.short";
    public static final String LONGTIMERBROADCAST = "com.example.broadcast.long";

    public static final String POMOCOMPLETEDBROADCAST = "com.example.completed.pomo";
    public static final String SHORTCOMPLETEDBROADCAST = "com.example.completed.short";
    public static final String LONGCOMPLETEDBROADCAST = "com.example.completed.long";

    public static final String DAILYBROADCAST = "com.example.broadcast.daily";

    public static final int TIMEMAX = 24;//120 mins
    public static final int TIMEMIN = 1;

    public static final String ADDEDIT_EXTRA_TOPIC_NAME = "com.example.timerdemo.EXTRA_TOPIC_NAME";
    public static final String ADDEDIT_EXTRA_TOPIC_TOTAL_MIN = "com.example.timerdemo.EXTRA_TOPIC_TOTAL_MIN";
    public static final String ADDEDIT_EXTRA_TOPIC_GOAL = "com.example.timerdemo.EXTRA_TOPIC_GOAL";
    public static final String ADDEDIT_EXTRA_TOPIC_ID = "com.example.timerdemo.EXTRA_TOPIC_ID";
    //TODO: there should also be DAYS_OF_WEEK as well or something like that

    public static final String DELETE_EXTRA_TOPIC = "com.example.timerdemo.EXTRA_DELETE";
    public static final String ADDEDIT_EXTRA_TOPIC_INDEX_POSITION = "com.example.timerdemo.EXTRA_INDEX_POSITION";

    //determines if the TopicListAddEditDeleteFragment should be add or edit fragment
    public static final int ADD_TOPIC_REQUEST = 1;
    public static final int EDIT_TOPIC_REQUEST = 2;

    public static final String SHAREDPREFS_DAILY_TIME = "com.example.timerdemo.DAILY_TIME";
    public static final String SHAREDPREFS = "com.example.timerdemo.sharedprefs";

    public static final String STUDIEDTODAYTEXT = "Studied Today: ";

    public static final String TIMERTYPE_POMO = "com.example.timerdemo.timertype.pomo";
    public static final String TIMERTYPE_LONG = "com.example.timerdemo.timertype.long";
    public static final String TIMERTYPE_SHORT = "com.example.timerdemo.timertype.short";

    public static final String TAG ="ttag";

}
