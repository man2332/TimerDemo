package com.example.timerdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class first extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent main = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(main);
                finish();
            }

        }, 1200);
    }
}
