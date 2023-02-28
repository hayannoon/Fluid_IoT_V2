package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.FileNotFoundException;


public class CreateGroup extends Activity {

    final static String CREATE_GROUP_DEBUGGING_TAG = "IOT_CreateGroup";
    final static String AUTH_CONFIGURATION_FILE = "configuration.json";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        Button createGroupSendButton = (Button) findViewById(R.id.group_create_send_btn);
        createGroupSendButton.setOnClickListener(new View.OnClickListener() { //Create Group
            @Override
            public void onClick(View view) {
                Log.d(CREATE_GROUP_DEBUGGING_TAG, "create new group button clicked");

                EditText groupID = (EditText) findViewById(R.id.group_name_input);
                CheckBox bulb1 = (CheckBox) findViewById(R.id.checkBox_bulb1);
                CheckBox bulb2 = (CheckBox) findViewById(R.id.checkBox2_bulb2);
                CheckBox ledStrip = (CheckBox) findViewById(R.id.checkBox3_strip);
                CheckBox camera = (CheckBox) findViewById(R.id.checkBox4_camera);
                CheckBox speaker = (CheckBox) findViewById(R.id.checkBox5_speaker);

                Auth auth = new Auth(groupID.getText().toString());
                auth.setBulb1(bulb1.isChecked());
                auth.setBulb2(bulb2.isChecked());
                auth.setLedStrip(ledStrip.isChecked());
                auth.setCamera(camera.isChecked());
                auth.setSpeaker(speaker.isChecked());

                //Get Json Data -> save to String type variable named jsonData
                JsonParser jp = new JsonParser(CreateGroup.this); //make a json parser instance
                //call add_jsonParseer function

                try { //Add group and write the config file.
                    Toast t;
                    if(jp.addConfigFile(auth, getApplicationContext())){
                        //추가에 성공한 경우
                        t = Toast.makeText(getApplicationContext(), "Create Group Success!", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                        t.show();
                    } else{
                        //실패한 경우
                        t = Toast.makeText(getApplicationContext(), "Create Group Failed", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
                        t.show();

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();

//
//                String jsonData = null;
//                try {
//                    jsonData = jp.getJsonString(JsonParser.AUTH_CONFIGURATION_FILE, getApplicationContext());
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } //Get json String and saved to the jsonData variable
//
//                try {
//                    jp.jsonParsing(jsonData);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }


            }
        });

    }
}
