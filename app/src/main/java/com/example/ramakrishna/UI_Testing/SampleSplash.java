package com.example.ramakrishna.UI_Testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.ramakrishna.R;
import com.example.ramakrishna.UI_Testing.activities.NavigationActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SampleSplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_splash);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SampleSplash.this, NavigationActivity.class));
                finish();
            }
        },3000);

    }
}