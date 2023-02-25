package com.example.iot_master_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.List;

public class ManageConfigurationDetail extends Activity implements Serializable {

    final static String JSONPARSER_DEBUGGING_TAG = "IOT_ManageConfigurationDetail";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(JSONPARSER_DEBUGGING_TAG,"auth debugging");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_configuration_detail);

        Intent intent = getIntent();
        int position = (int) intent.getSerializableExtra("SELECTED_GROUP");
        Log.d(JSONPARSER_DEBUGGING_TAG,"auth debugging" + "position : " + position);

        //다시 제이슨을 읽고, position번째 객체 정보를 보여준다.

        JsonParser jp = new JsonParser(ManageConfigurationDetail.this);
        List<Auth> authList = null;
        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } //authList에는 json을 파싱해서 클래스 배열로 만든 결과가 담겨있음


        Auth selectedAuthObject = authList.get(position); //유저가 선택한 group으로 진입


        if(selectedAuthObject != null){
            TextView groupID = (TextView) findViewById(R.id.textView_groupname_config);
            CheckBox bulb1 = (CheckBox) findViewById(R.id.checkBox_bulb1_config);
            CheckBox bulb2 = (CheckBox) findViewById(R.id.checkBox_bulb2_config);
            CheckBox ledStrip = (CheckBox) findViewById(R.id.checkBox_strip_config);
            CheckBox camera = (CheckBox) findViewById(R.id.checkBox_camera_config);
            CheckBox speaker = (CheckBox) findViewById(R.id.checkBox_speaker_config);

            groupID.setText(selectedAuthObject.getGroupID());
            bulb1.setChecked(selectedAuthObject.isBulb1());
            bulb2.setChecked(selectedAuthObject.isBulb2());
            ledStrip.setChecked(selectedAuthObject.isLedStrip());
            camera.setChecked(selectedAuthObject.isCamera());
            speaker.setChecked(selectedAuthObject.isSpeaker()); //기존의 권한 상태를 보여준다.


            Button updateConfigurationButton = (Button) findViewById(R.id.update_config_btn);
            updateConfigurationButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Log.d(JSONPARSER_DEBUGGING_TAG,"UPDATE CONFIG BUTTON CLICKED!!!");



                    //여기서 각각의 Checkbox 값 읽어들여서 json파일 다시 쓰면 된다
                    Auth newAuth = new Auth(bulb1.isChecked(), bulb2.isChecked(), ledStrip.isChecked(), camera.isChecked(), speaker.isChecked());
                    try {
                        Toast t;
                        if(jp.updateConfigFile(position, newAuth, getApplicationContext())){
                            t = Toast.makeText(getApplicationContext(), "Update Group Success!", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            t.show();
                        }else{
                            t = Toast.makeText(getApplicationContext(), "Update Group Success!", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            t.show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button deleteGroupButton = (Button) findViewById(R.id.delete_group_btn);
            deleteGroupButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Log.d(JSONPARSER_DEBUGGING_TAG,"DELETE GROUP BUTTON CLICKED!!!");
                    Toast t;
                    try {
                        if(jp.removeGroup(position, getApplicationContext())){
                            t = Toast.makeText(getApplicationContext(), "Delete Group Success!", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            t.show();
                        } else{
                            t = Toast.makeText(getApplicationContext(), "Delete Group Failed", Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                            t.show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
//                    Intent intent = new Intent(getApplicationContext(), ManageConfiguration.class);
//                    startActivity(intent);

                }
            });



        }



    }
}
