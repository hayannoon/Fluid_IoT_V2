package com.example.iot_master_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import java.util.concurrent.ExecutionException;

public class ManageConfigurationDetail extends Activity implements Serializable {

    final static String ManageConfigurationDetail_DEBUGGING_TAG = "IOT_ManageConfigurationDetail";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(ManageConfigurationDetail_DEBUGGING_TAG,"auth debugging");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_configuration_detail);

        Intent intent = getIntent();
        int position = (int) intent.getSerializableExtra("SELECTED_GROUP");
        Log.d(ManageConfigurationDetail_DEBUGGING_TAG,"auth debugging" + "position : " + position);

        //다시 제이슨을 읽고, position번째 객체 정보를 보여준다.

        JsonParser jp = new JsonParser(ManageConfigurationDetail.this);
        List<Auth> authList = null;
        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } //authList에는 json을 파싱해서 클래스 배열로 만든 결과가 담겨있음
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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
                    Log.d(ManageConfigurationDetail_DEBUGGING_TAG,"UPDATE CONFIG BUTTON CLICKED!!!");



                    //여기서 각각의 Checkbox 값 읽어들여서 json파일 다시 쓰면 된다
                    Auth newAuth = new Auth(bulb1.isChecked(), bulb2.isChecked(), ledStrip.isChecked(), camera.isChecked(), speaker.isChecked());
                    try {
                        Toast t;
                        if(jp.updateConfigFile(position, newAuth, getApplicationContext())){
                            //추가에 성공한 경우
                            new AlertDialog.Builder(ManageConfigurationDetail.this)
                                    .setTitle("[SUCESS]")
                                    .setMessage("GROUP UPDATE SUCESS!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    }).create().show();
                        }else{
                            //실패한 경우
                            new AlertDialog.Builder(ManageConfigurationDetail.this)
                                    .setTitle("[FAILED]")
                                    .setMessage("GROUP UPDATE FAILED \nTRY AGAIN!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    }).create().show();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button deleteGroupButton = (Button) findViewById(R.id.delete_group_btn);
            deleteGroupButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Log.d(ManageConfigurationDetail_DEBUGGING_TAG,"DELETE GROUP BUTTON CLICKED!!!");

                    new AlertDialog.Builder(ManageConfigurationDetail.this)
                            .setTitle("[DELETE A GROUP]")
                            .setMessage("If you click yes, this group will be deleted. \ncontinue?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if(jp.removeGroup(position, getApplicationContext())){
                                            new AlertDialog.Builder(ManageConfigurationDetail.this)
                                                    .setTitle("[SUCESS]")
                                                    .setMessage("GROUP DELETE SUCESS!")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            finish();
                                                        }
                                                    }).create().show();
                                            Toast.makeText(getApplicationContext(), "DELETE GROUP COMPLETE", Toast.LENGTH_SHORT).show();

                                        } else{
                                            //실패한 경우
                                            new AlertDialog.Builder(ManageConfigurationDetail.this)
                                                    .setTitle("[FAILED]")
                                                    .setMessage("GROUP DELETE FAILED \nTRY AGAIN!")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                        }
                                                    }).create().show();
                                            Toast.makeText(getApplicationContext(), "DELETE GROUP FAILED", Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "DELETE GROUP CANCELED", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create().show();

//
//                    try {
//                        if(jp.removeGroup(position, getApplicationContext())){
//                            new AlertDialog.Builder(ManageConfigurationDetail.this)
//                                    .setTitle("[SUCESS]")
//                                    .setMessage("GROUP DELETE SUCESS!")
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            finish();
//                                        }
//                                    }).create().show();
//                        } else{
//                            //실패한 경우
//                            new AlertDialog.Builder(ManageConfigurationDetail.this)
//                                    .setTitle("[FAILED]")
//                                    .setMessage("GROUP DELETE FAILED \nTRY AGAIN!")
//                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                        }
//                                    }).create().show();
//                        }
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
                    //finish();
//                    Intent intent = new Intent(getApplicationContext(), ManageConfiguration.class);
//                    startActivity(intent);

                }


            });



        }



    }
}
