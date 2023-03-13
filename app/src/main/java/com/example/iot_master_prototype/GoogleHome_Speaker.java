package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class GoogleHome_Speaker extends Activity {

    final static String SPEAKER = "speaker";
    final static String SPEAKER_LOG = "[FLUID-IoT]SPEAKER";


    private void setBackgroundImage(boolean volume, boolean mute, boolean onOff, boolean startStop){
        TextView toggleView = (TextView) findViewById(R.id.speaker_toggle_textview);
        ImageView backgroundImage = (ImageView) findViewById(R.id.speaker_imageView);
        boolean[] boolArray = {startStop, onOff, mute, volume};

        int decimalValue = 0;
        for (int i = 0; i < boolArray.length; i++) {
            if (boolArray[i]) {
                decimalValue += Math.pow(2, i);
            }
        }

        switch (decimalValue) {
            case 0:
                // All variables are false
                toggleView.setText("NO ACCESS");
                backgroundImage.setImageResource(R.drawable.nothing);
                break;
            case 1:
                // Only the start_stop variable is true
                toggleView.setText("Only Start/Stop");
                backgroundImage.setImageResource(R.drawable.onlystartstop);
                break;
            case 2:
                // Only the on_off variable is true
                toggleView.setText("Only On/Off");
                backgroundImage.setImageResource(R.drawable.onlyonoff);
                break;
            case 3:
                // Both the on_off and start_stop variables are true
                toggleView.setText("On_Off&Start_Stop");
                backgroundImage.setImageResource(R.drawable.on_off_start_stop);
                break;
            case 4:
                // Only the mute variable is true
                toggleView.setText("Only Mute");
                backgroundImage.setImageResource(R.drawable.onlymute);
                break;
            case 5:
                // Both the mute and start_stop variables are true
                toggleView.setText("Mute&Start/Stop");
                backgroundImage.setImageResource(R.drawable.mute_start_stop);
                break;
            case 6:
                // Both the mute and on_off variables are true
                toggleView.setText("Mute&On/Off");
                backgroundImage.setImageResource(R.drawable.mute_on_off);
                break;
            case 7:
                // All variables except volume are true
                toggleView.setText("All but Volume");
                backgroundImage.setImageResource(R.drawable.except_volume);
                break;
            case 8:
                // Only the volume variable is true
                toggleView.setText("Only Volume");
                backgroundImage.setImageResource(R.drawable.onlyvolume);
                break;
            case 9:
                // Both the volume and start_stop variables are true
                toggleView.setText("Volume&Start_Stop");
                backgroundImage.setImageResource(R.drawable.volume_start_stop);
                break;
            case 10:
                // Both the volume and on_off variables are true
                toggleView.setText("Volume&On/Off");
                backgroundImage.setImageResource(R.drawable.volume_on_off);
                break;
            case 11:
                // All variables except mute are true
                toggleView.setText("All but Mute");
                backgroundImage.setImageResource(R.drawable.except_mute);
                break;
            case 12:
                // Both the volume and mute variables are true
                toggleView.setText("Volume&Mute");
                backgroundImage.setImageResource(R.drawable.volume_mute);
                break;
            case 13:
                // All variables except on_off are true
                toggleView.setText("All but On/Off");
                backgroundImage.setImageResource(R.drawable.except_on_off);
                break;
            case 14:
                // All variables except start_stop are true
                toggleView.setText("All but Start/Stop");
                backgroundImage.setImageResource(R.drawable.except_start_stop);
                break;
            case 15:
                // All variables are true
                toggleView.setText("FULL ACCESS");
                backgroundImage.setImageResource(R.drawable.all);
                break;
            default:
                toggleView.setText("ERROR");
                backgroundImage.setImageResource(R.drawable.nothing);
                // This should not be reached, but handle it just in case
                break;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_home_speaker);

        JsonParser jp = new JsonParser();
        List<Auth> authList = null;

        ImageView backgroundImage = (ImageView) findViewById(R.id.speaker_imageView);
        boolean[] volumeIsChecked = {false};
        boolean[] muteIsChecked = {false};
        boolean[] onOffIsChecked = {false};
        boolean[] startStopIsChecked = {false};
        String[] supervisedBy = {null};

        Button volumeButton = (Button) findViewById(R.id.speaker_volume_button); //volume button
        Button muteButton = (Button) findViewById(R.id.speaker_mute_button);
        Button onOffButton = (Button) findViewById(R.id.speaker_on_off_button);
        Button startStopButton = (Button) findViewById(R.id.speaker_start_stop_button);




        volumeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(SPEAKER_LOG, "VOLUME BUTTON CLICK!");
                volumeIsChecked[0] = !volumeIsChecked[0];

                setBackgroundImage(volumeIsChecked[0], muteIsChecked[0], onOffIsChecked[0] , startStopIsChecked[0]);
            }
        });


        muteButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(SPEAKER_LOG, "MUTE BUTTON CLICK!");
                muteIsChecked[0] = !muteIsChecked[0];
                setBackgroundImage(volumeIsChecked[0], muteIsChecked[0], onOffIsChecked[0] , startStopIsChecked[0]);

            }
        });


        onOffButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(SPEAKER_LOG, "ON OFF BUTTON CLICK!");
                onOffIsChecked[0] = !onOffIsChecked[0];
                setBackgroundImage(volumeIsChecked[0], muteIsChecked[0], onOffIsChecked[0] , startStopIsChecked[0]);

            }
        });


        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(SPEAKER_LOG, "START STOP BUTTON CLICK!");
                startStopIsChecked[0] = !startStopIsChecked[0];
                setBackgroundImage(volumeIsChecked[0], muteIsChecked[0], onOffIsChecked[0] , startStopIsChecked[0]);

            }
        });

    }
}
