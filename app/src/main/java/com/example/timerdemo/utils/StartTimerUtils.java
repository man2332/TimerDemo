package com.example.timerdemo.utils;

import android.content.Context;
import android.content.Intent;

import com.example.timerdemo.TimerService;

public class StartTimerUtils {
    public static void startTimer(long duration, Context context){
        Intent serviceIntent = new Intent(context, TimerService.class);
    }
}
