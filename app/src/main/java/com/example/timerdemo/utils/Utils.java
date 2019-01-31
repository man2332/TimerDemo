package com.example.timerdemo.utils;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Utils {
    public static String convertMilisToTimeFormat(long millisUntilFinished){
        return String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
    }
}
