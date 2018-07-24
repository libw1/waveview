package com.example.libowen.waveviewdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WaveView waveView = findViewById(R.id.wave_view);
        ObjectAnimator animator = ObjectAnimator.ofInt(waveView,"dx",0,waveView.getWaveLength());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000);
        animator.start();
    }
}
