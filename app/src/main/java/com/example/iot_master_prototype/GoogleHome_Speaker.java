package com.example.iot_master_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GoogleHome_Speaker extends Activity implements AdapterView.OnItemSelectedListener{

    String[] items;
    String[] items1;

    final static String SPEAKER = "speaker";
    final static String SPEAKER_LOG = "[FLUID-IoT]SPEAKER";

    Spinner groupSelectSpinner;
    Spinner supervisedGroupSelectSpinner;


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
        boolean[] isSupervisedIsChecked = {false};
        boolean[] isTemporalIsChecked = {false};
        String[] supervisedBy = {null};

        Button volumeButton = (Button) findViewById(R.id.speaker_volume_button); //volume button
        Button muteButton = (Button) findViewById(R.id.speaker_mute_button);
        Button onOffButton = (Button) findViewById(R.id.speaker_on_off_button);
        Button startStopButton = (Button) findViewById(R.id.speaker_start_stop_button);

        TableRow supervisedSettingTableRow = (TableRow) findViewById(R.id.speaker_supervisedSetting);
        LinearLayout timePickerWrapper = (LinearLayout) findViewById(R.id.speaker_time_picker_wrapper_layout);

        EditText startTimeTextEdit = (EditText) findViewById(R.id.speaker_start_time);
        EditText endTImeTextEdit = (EditText) findViewById(R.id.speaker_end_time);

        TableRow temporalSettingTableRow1 = (TableRow) findViewById(R.id.speaker_start_time_wrapper);
        TableRow temporalSettingTableRow2 = (TableRow) findViewById(R.id.speaker_end_time_wrapper);
        TimePicker startTimePicker = (TimePicker) findViewById(R.id.speaker_start_time_picker);
        TimePicker endTimePicker = (TimePicker) findViewById(R.id.speaker_end_time_picker);

        Switch speakerSupervisedPermissionSwitch = (Switch) findViewById(R.id.speaker_supervised_permission_switch);
        Switch speakerTemporalPermissionSwitch = (Switch) findViewById(R.id.speaker_temporal_permssion_switch);


        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> groupIDList = new ArrayList<>();
        for(Auth auth : authList){
            groupIDList.add(auth.getGroupID());
        }//여기까지 오면 Group ID LIST 생성

        items = (String[]) groupIDList.toArray(new String[0]);
        groupSelectSpinner = (Spinner) findViewById(R.id.speaker_group_select_spinner);
        groupSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Auth> authListFromServer = null;
                try {
                    authListFromServer = jp.getAuthListFromCOnfigFile_v2();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Auth auth = authListFromServer.get(position);
                //Group을 사용자가 변경할떄마다 auth에 저장된 그룹 인스턴스가 변경된다.

                volumeIsChecked[0] = auth.isSpeakerVolume();
                muteIsChecked[0] = auth.isSpeakerMute();
                onOffIsChecked[0] = auth.isSpeakerOnOff();
                startStopIsChecked[0] = auth.isSpeakerStartStop();
                isSupervisedIsChecked[0] = auth.isSpeakerSupervised();
                isTemporalIsChecked[0] = auth.isSpeakerIsTemporal();
                supervisedBy[0] = auth.getSpeakerSupervisedBy();


                setBackgroundImage(volumeIsChecked[0], muteIsChecked[0], onOffIsChecked[0] , startStopIsChecked[0]);

                if(isSupervisedIsChecked[0]){ //supervised가 켜져있는 경우
                    int index = 0;
                    for(int j = 0 ; j < authListFromServer.size() ; j++){
                        String tmpGroup = authListFromServer.get(j).getGroupID();
                        if(supervisedBy[0].equals(tmpGroup)){
                            index = j;
                        }
                    }

                    speakerSupervisedPermissionSwitch.setChecked(true);
                    supervisedSettingTableRow.setVisibility(View.VISIBLE);
                    supervisedGroupSelectSpinner.setSelection(index);
                }else{
                    speakerSupervisedPermissionSwitch.setChecked(false);
                    supervisedSettingTableRow.setVisibility(View.GONE);
                } //set supervised permission

                if(isTemporalIsChecked[0]){
                    String start, end;
                    speakerTemporalPermissionSwitch.setChecked(true);
                    start = auth.getSpeakerStartTime();
                    end = auth.getSpeakerEndTime();

                    EditText startTimeTextEdit = (EditText) findViewById(R.id.speaker_start_time);
                    EditText endTimeTextEdit = (EditText) findViewById(R.id.speaker_end_time);
                    startTimeTextEdit.setText(start);
                    endTimeTextEdit.setText(end);

                    int parsingChar = start.indexOf(":");
                    startTimePicker.setHour(Integer.parseInt(start.substring(0, parsingChar).trim()));
                    startTimePicker.setMinute(Integer.parseInt(start.substring(parsingChar + 1).trim()));

                    parsingChar = end.indexOf(":");
                    endTimePicker.setHour(Integer.parseInt(end.substring(0, parsingChar).trim()));
                    endTimePicker.setMinute(Integer.parseInt(end.substring(parsingChar + 1).trim()));
                } else{
                    speakerTemporalPermissionSwitch.setChecked(false);
                } //Time Picker 설정 완료

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Supervised Permission setting

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSelectSpinner.setAdapter(adapter);

        items1 = (String[]) groupIDList.toArray(new String[0]); // List to Array

        supervisedGroupSelectSpinner = (Spinner) findViewById(R.id.speaker_supervised_group_select_spinner);
        supervisedGroupSelectSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supervisedGroupSelectSpinner.setAdapter(adapter1);

        //SETTING START

        List<Auth> authListFromServer = null;
        String firstGroup = groupSelectSpinner.getSelectedItem().toString();
        try {
            authListFromServer = jp.getAuthListFromCOnfigFile_v2();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0 ; i < authListFromServer.size() ; i++) {
            Auth auth = authListFromServer.get(i);
            if(auth.getGroupID().equals(firstGroup)){
                String supervisedByName;
                String start;
                String end;

                volumeIsChecked[0] = auth.isSpeakerVolume();
                muteIsChecked[0] = auth.isSpeakerMute();
                onOffIsChecked[0] = auth.isSpeakerOnOff();
                supervisedByName = auth.getSpeakerSupervisedBy();
                startStopIsChecked[0] = auth.isSpeakerStartStop();
                start = auth.getSpeakerStartTime();
                end = auth.getSpeakerEndTime();

                if(isSupervisedIsChecked[0]){
                    int index = 0;
                    for(int j = 0 ; j < authListFromServer.size() ; j++){
                        if(supervisedByName.equals(authListFromServer.get(j).getGroupID())){
                            index = j;
                        }
                    }//그룹이 속한 index 찾기
                    speakerSupervisedPermissionSwitch.setChecked(true);
                    supervisedSettingTableRow.setVisibility(View.VISIBLE);
                    supervisedGroupSelectSpinner.setSelection(index);
                } else{
                    speakerSupervisedPermissionSwitch.setChecked(false);
                    supervisedSettingTableRow.setVisibility(View.GONE);
                } //set supervised permission

                if(isTemporalIsChecked[0]){
                    speakerTemporalPermissionSwitch.setChecked(true);
                    startTimeTextEdit = (EditText) findViewById(R.id.speaker_start_time);
                    startTimeTextEdit.setText(start);

                    endTImeTextEdit = (EditText) findViewById(R.id.speaker_end_time);
                    endTImeTextEdit.setText(end);

                    int parsingChar = start.indexOf(":");
                    startTimePicker.setHour(Integer.parseInt(start.substring(0, parsingChar).trim()));
                    startTimePicker.setMinute(Integer.parseInt(start.substring(parsingChar + 1).trim()));

                    parsingChar = end.indexOf(":");
                    endTimePicker.setHour(Integer.parseInt(end.substring(0, parsingChar).trim()));
                    endTimePicker.setMinute(Integer.parseInt(end.substring(parsingChar + 1).trim()));
                } else{
                    speakerTemporalPermissionSwitch.setChecked(false);
                }
                break;
            }

        }

        if(speakerSupervisedPermissionSwitch.isChecked()){
            supervisedSettingTableRow.setVisibility(View.VISIBLE);
        }else{
            supervisedSettingTableRow.setVisibility(View.GONE);
        }
        speakerSupervisedPermissionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    supervisedSettingTableRow.setVisibility(View.VISIBLE);
                }else{
                    supervisedSettingTableRow.setVisibility(View.GONE);
                }
            }
        });

        if(speakerTemporalPermissionSwitch.isChecked()){
            temporalSettingTableRow1.setVisibility(View.VISIBLE);
            temporalSettingTableRow2.setVisibility(View.VISIBLE);
            timePickerWrapper.setVisibility(View.VISIBLE);
        } else{
            temporalSettingTableRow1.setVisibility(View.GONE);
            temporalSettingTableRow2.setVisibility(View.GONE);
            timePickerWrapper.setVisibility(View.GONE);
        }


        speakerTemporalPermissionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    temporalSettingTableRow1.setVisibility(View.VISIBLE);
                    temporalSettingTableRow2.setVisibility(View.VISIBLE);
                    timePickerWrapper.setVisibility(View.VISIBLE);
                } else{
                    temporalSettingTableRow1.setVisibility(View.GONE);
                    temporalSettingTableRow2.setVisibility(View.GONE);
                    timePickerWrapper.setVisibility(View.GONE);
                }
            }
        });

        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                EditText startTimeTextEdit = (EditText) findViewById(R.id.speaker_start_time);
                startTimeTextEdit.setText(hourOfDay +" : " + minute);
            }
        });

        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                EditText endTimeTextEdit = (EditText) findViewById(R.id.speaker_end_time);
                endTimeTextEdit.setText(hourOfDay + " : " + minute);
            }
        });


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

        Button saveButton = (Button) findViewById(R.id.speaker_detail_save_button);
        EditText finalStartTimeTextEdit = startTimeTextEdit;
        EditText finalEndTimeTextEdit = endTImeTextEdit;
        saveButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(GoogleHome_Speaker.this)
                        .setTitle("[SAVE AUTHORITY]")
                        .setMessage("If you click yes, this authrority information will be saved.\ncontinue?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //DO SAVE ~~~~
                                String groupNameForUpdate = groupSelectSpinner.getSelectedItem().toString();
                                int index = groupSelectSpinner.getSelectedItemPosition();

                                Auth newAuth = new Auth(groupNameForUpdate);
                                //group의 index와 해당 devcie의 정보만 채워서 보낸다.

                                if(onOffIsChecked[0] | volumeIsChecked[0] | startStopIsChecked[0] | muteIsChecked[0]){
                                    newAuth.setSpeaker(true);
                                } else{
                                    newAuth.setSpeaker(false);
                                }

                                newAuth.setSpeakerVolume(volumeIsChecked[0]);
                                newAuth.setSpeakerMute(muteIsChecked[0]);
                                newAuth.setSpeakerOnOff(onOffIsChecked[0]);
                                newAuth.setSpeakerStartStop(startStopIsChecked[0]);
                                newAuth.setSpeakerSupervised(speakerSupervisedPermissionSwitch.isChecked());
                                if(!speakerSupervisedPermissionSwitch.isChecked()){
                                    newAuth.setSpeakerSupervisedBy("None");
                                }else{
                                    newAuth.setSpeakerSupervisedBy(supervisedGroupSelectSpinner.getSelectedItem().toString());
                                }

                                newAuth.setSpeakerIsTemporal(speakerTemporalPermissionSwitch.isChecked());
                                newAuth.setSpeakerStartTime(finalStartTimeTextEdit.getText().toString());
                                newAuth.setSpeakerEndTime(finalEndTimeTextEdit.getText().toString());

                                try {
                                    jp.updateConfigFile_V2_SPEAKER(index,newAuth);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    jp.updateConfigFile(index,newAuth,getApplicationContext());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                                Toast.makeText(getApplicationContext(), "UPDATE AUTH INFO COMPLETE", Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "CANCEL AUTH INFO", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .create().show();
            }
        });

    }


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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
