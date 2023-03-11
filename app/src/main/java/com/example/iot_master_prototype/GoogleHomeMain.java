package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class GoogleHomeMain extends Activity {

    final static String GOOGLE_HOME_DEGUGGING_TAG = "IoT_Google_Home_Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_home_main);

        Button ledStripButton = (Button) findViewById(R.id.LED_strip_button);
        ledStripButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG,"click LED!");
                //DO SOmetihng!!!
            }
        });


        Button rapoBulbButton = (Button) findViewById(R.id.rapo_smart_bulb_button);
        rapoBulbButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG,"click Rapo Bulb!");
                //DO SOmetihng!!!
            }
        });


        Button smartLEDStandButon = (Button) findViewById(R.id.smart_led_stand_button);
        smartLEDStandButon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG,"click LED STAND!");
                //DO SOmetihng!!!
            }
        });


        Button homeCameraButton = (Button) findViewById(R.id.home_camera_button);
        homeCameraButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG,"click Camera!");
                //DO SOmetihng!!!
            }
        });


        Button speakerButton = (Button) findViewById(R.id.galaxy_home_mini_button);
        speakerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG,"click Speaker!");
                //DO SOmetihng!!!
            }
        });

    }
}
