package com.example.trackinjectv2.Injections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.trackinjectv2.R;

public class InjectionsMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injections_map_acrivity);

        //Background Animation code
        ScrollView relativeLayout = findViewById(R.id.injectionsMapLayout);
        AnimationDrawable animationDrawable =  (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();


    }
}