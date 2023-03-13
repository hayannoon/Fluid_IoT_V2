package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class GoogleHome_Speaker extends Activity {

    final static String SPEAKER = "speaker";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_home_speaker);
    }
}
