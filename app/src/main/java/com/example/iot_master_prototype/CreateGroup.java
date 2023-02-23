package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;

import org.json.JSONException;


public class CreateGroup extends Activity {

    final static String CREATE_GROUP_DEBUGGING_TAG = "IOT_CreateGroup";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        Button createGroupSendButton = (Button) findViewById(R.id.group_create_send_btn);
        createGroupSendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(CREATE_GROUP_DEBUGGING_TAG, "create new group button clicked");

                EditText groupID = (EditText) findViewById(R.id.group_name_input);
                CheckBox bulb1 = (CheckBox) findViewById(R.id.checkBox_bulb1);
                CheckBox bulb2 = (CheckBox) findViewById(R.id.checkBox2_bulb2);
                CheckBox ledStrip = (CheckBox) findViewById(R.id.checkBox3_strip);
                CheckBox camera = (CheckBox) findViewById(R.id.checkBox4_camera);
                CheckBox speaker = (CheckBox) findViewById(R.id.checkBox5_speaker);

                /*
                Log.d(CREATE_GROUP_DEBUGGING_TAG, "Group ID : " + groupID.getText().toString());
                Log.d(CREATE_GROUP_DEBUGGING_TAG, "bulb1 : " + bulb1.isChecked());
                Log.d(CREATE_GROUP_DEBUGGING_TAG, "bulb2 : " + bulb2.isChecked());
                */

                //Get Json Data -> save to jsonData variable
                JsonParser jp = new JsonParser(CreateGroup.this);
                String jsonData = jp.getJsonString("configuration.json");
                jp.jsonParsing(jsonData);

                try {
                    jp.changeGroupName("level1", "level111");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jp.jsonParsing(jsonData);



            }

        });

    }


}
