package com.example.iot_master_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GoogleHome_LED_Strip extends Activity implements Serializable, AdapterView.OnItemSelectedListener {

    String[] items;
    String[] items1;
    Spinner groupSelectSpinner;
    Spinner supervisedGroupSelecterSpinner;

    String authStatus = NOTHING;


    final static String BULB_1 = "bulb1";
    final static String BULB_2 = "bulb2";
    final static String STRIP = "strip";

    final static String ONLY_ON_OFF = "ON/OFF CONTROL";
    final static String ONLY_BRIGHTNESS = "BRIGHTNESS CONTROL";
    final static String BOTH = "FULL ACCESS";
    final static String NOTHING = "NO ACCESS";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_home_led_strip); //Bulb 배경 깔고

        JsonParser jp = new JsonParser();
        List<Auth> authList = null;

        Intent intent = getIntent();
        String selectedDevice = (String) intent.getSerializableExtra("SELECTED_DEVICE");


        //Get the instance from ID

        final Button[] onOffButton = {(Button) findViewById(R.id.led_on_off_button)};
        ImageView backroundImage = (ImageView) findViewById(R.id.strip_bulb_imageView);
        boolean[] onOffIsChecked = {false};
        boolean[] brightnessIsChecked = {false};
        boolean[] isSupervisedIsChecked = {false};
        boolean[] isTemporalIsChecked = {false};
        String[] supervisedBy = {null};

        Button ledStripBrightnessButton1 = (Button) findViewById(R.id.led_strip_brightness_button1);
        Button ledStripBrightnessButton2 = (Button) findViewById(R.id.led_strip_brightness_button2);
        Button ledStripBrightnessButton3 = (Button) findViewById(R.id.led_strip_brightness_button3);
        Button ledStripBrightnessButton4 = (Button) findViewById(R.id.led_strip_brightness_button4);

        TableRow supervisedSettingRableRow = (TableRow) findViewById(R.id.strip_supervisedSetting);
        Switch stripSupervisedPermissionSwitch = (Switch) findViewById(R.id.strip_supervised_permission_switch);

        LinearLayout timePickerWapper = (LinearLayout) findViewById(R.id.strip_time_picker_wapper_layout);

        TableRow temporalSettingTableRow1 = (TableRow) findViewById(R.id.strip_start_time_wrapper);
        TableRow temporalSettingTableRow2 = (TableRow) findViewById(R.id.strip_end_time_wrapper);
        TimePicker startTimePicker = (TimePicker) findViewById(R.id.strip_start_time_picker);
        TimePicker endTimePicker = (TimePicker) findViewById(R.id.strip_end_time_picker);

        Switch temporalPermissionSwitch = (Switch) findViewById(R.id.temporal_permission_switch);

        EditText startTimeTextEdit = (EditText) findViewById(R.id.strip_start_time);
        EditText endTimeTextEdit = (EditText) findViewById(R.id.strip_end_time);

        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext()); //
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }//authList에는 json을 파싱해서 클래스 배열로 만든 결과가 담겨있고 여기는 모든 그룹의 권한 정보가 있음.

        List<String> groupIDList = new ArrayList<>();
        for (Auth auth : authList) {
            groupIDList.add(auth.getGroupID());
        } //여기까지 오면 GroupIDList 완성

        items = (String[]) groupIDList.toArray(new String[0]); // List to Array
        groupSelectSpinner = (Spinner) findViewById(R.id.led_strip_group_select_spinner);

        groupSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Group선택 Spinner에 항목 세팅
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Auth> authListFromServer = null;
                String firtst_group = groupSelectSpinner.getSelectedItem().toString();
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
                // Group을 사용자가 변경할때마다 auth에 저장된 그룹 인스턴스가 해당 그룹에 맞게 변경된다.
                //Do many things;


                if (selectedDevice.equals(STRIP)) { //STRIP인 경우
                    onOffIsChecked[0] = auth.isLedStripOnOff();
                    brightnessIsChecked[0] = auth.isLedStripBrightness();
                    isSupervisedIsChecked[0] = auth.isLedStripIsSupervised();
                    supervisedBy[0] = auth.getLedStripSupervisedBy();
                    isTemporalIsChecked[0] = auth.isLedStripIsTemporal();
                } else if (selectedDevice.equals(BULB_1)) {
                    onOffIsChecked[0] = auth.isBulb1OnOff();
                    brightnessIsChecked[0] = auth.isBulb1Brightness();
                    isSupervisedIsChecked[0] = auth.isBulb1IsSupervised();
                    supervisedBy[0] = auth.getBulb1SupervisedBy();
                    isTemporalIsChecked[0] = auth.isBulb1IsTemporal();
                } else if (selectedDevice.equals(BULB_2)) {
                    onOffIsChecked[0] = auth.isBulb2OnOff();
                    brightnessIsChecked[0] = auth.isBulb2Brightness();
                    isSupervisedIsChecked[0] = auth.isBulb2IsSupervised();
                    supervisedBy[0] = auth.getBulb2SupervisedBy();
                    isTemporalIsChecked[0] = auth.isBulb2IsTemporal();
                }

                //공통 로직
                if (onOffIsChecked[0]) { //사용자가 클릭할때마다 배경 변경을 위한 코드
                    if (brightnessIsChecked[0]) {
                        authStatus = selectedDevice + "\n" + BOTH;
                        backroundImage.setImageResource(R.drawable.bulb_both);
                    } else {
                        authStatus = selectedDevice + "\n" + ONLY_ON_OFF;
                        backroundImage.setImageResource(R.drawable.bulb_onoff);
                    }
                } else {
                    if (brightnessIsChecked[0]) {
                        authStatus = selectedDevice + "\n" + ONLY_BRIGHTNESS;
                        backroundImage.setImageResource(R.drawable.bulb_brightness);
                    } else {
                        authStatus = selectedDevice + "\n" + NOTHING;
                        backroundImage.setImageResource(R.drawable.bulb_specific);
                    }
                }
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                toggleView.setText(authStatus); //상단에 기기 이름 + 현재 권한 상태 설정

                if (isSupervisedIsChecked[0]) { //supervised가 켜져있는 경우
                    int index = 0;
                    for (int j = 0; j < authListFromServer.size(); j++) {
                        String tmpGroup = authListFromServer.get(j).getGroupID();
                        if (supervisedBy[0].equals(tmpGroup)) {
                            index = j;
                        }
                    } //Index를 찾는다.

                    stripSupervisedPermissionSwitch.setChecked(true);
                    supervisedSettingRableRow.setVisibility(View.VISIBLE);
                    supervisedGroupSelecterSpinner.setSelection(index);
                } else {
                    stripSupervisedPermissionSwitch.setChecked(false);
                    supervisedSettingRableRow.setVisibility(View.GONE);

                } //set supervised permission


                if (isTemporalIsChecked[0]) {
                    String start, end;
                    temporalPermissionSwitch.setChecked(true);

                    if (selectedDevice.equals(BULB_1)) {
                        start = auth.getBulb1StartTime();
                        end = auth.getBulb1EndTime();
                    } else if (selectedDevice.equals(BULB_2)) {
                        start = auth.getBulb2StartTime();
                        end = auth.getBulb2EndTime();
                    } else { //LED STRIP
                        start = auth.getLedStripStartTime();
                        end = auth.getLedStripEndTime();
                    }

                    EditText startTimeTextEdit = (EditText) findViewById(R.id.strip_start_time);
                    startTimeTextEdit.setText(start);

                    EditText endTimeTextEdit = (EditText) findViewById(R.id.strip_end_time);
                    endTimeTextEdit.setText(end);

                    int parsingChar = start.indexOf(":");
                    startTimePicker.setHour(Integer.parseInt(start.substring(0, parsingChar).trim()));
                    startTimePicker.setMinute(Integer.parseInt(start.substring(parsingChar + 1).trim()));

                    parsingChar = end.indexOf(":");
                    endTimePicker.setHour(Integer.parseInt(end.substring(0, parsingChar).trim()));
                    endTimePicker.setMinute(Integer.parseInt(end.substring(parsingChar + 1).trim()));
                } else {
                    temporalPermissionSwitch.setChecked(false);
                } //Time Picker 설정 완료

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        //Supervised Permission setting
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSelectSpinner.setAdapter(adapter);

        items1 = (String[]) groupIDList.toArray(new String[0]); // List to Array

        supervisedGroupSelecterSpinner = (Spinner) findViewById(R.id.led_strip_supervised_group_select_spinner);
        supervisedGroupSelecterSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supervisedGroupSelecterSpinner.setAdapter(adapter1);

        //SETTING START
        List<Auth> authListFromServer = null;
        Auth selectedAuth = null;
        String firtst_group = groupSelectSpinner.getSelectedItem().toString();
        try {
            authListFromServer = jp.getAuthListFromCOnfigFile_v2();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < authListFromServer.size(); i++) {
            Auth auth = authListFromServer.get(i);
            if (auth.getGroupID().equals(firtst_group)) { // ???
                //Do many things;
                String supervisedByName;
                String start;
                String end;

                if (selectedDevice.equals(BULB_1)) {
                    onOffIsChecked[0] = auth.isBulb1OnOff();
                    brightnessIsChecked[0] = auth.isBulb1Brightness();
                    isSupervisedIsChecked[0] = auth.isBulb1IsSupervised();
                    supervisedByName = auth.getBulb1SupervisedBy();
                    isTemporalIsChecked[0] = auth.isBulb1IsTemporal();
                    start = auth.getBulb1StartTime();
                    end = auth.getBulb1EndTime();
                } else if (selectedDevice.equals(BULB_2)) {
                    onOffIsChecked[0] = auth.isBulb2OnOff();
                    brightnessIsChecked[0] = auth.isBulb2Brightness();
                    isSupervisedIsChecked[0] = auth.isBulb2IsSupervised();
                    supervisedByName = auth.getBulb2SupervisedBy();
                    isTemporalIsChecked[0] = auth.isBulb2IsTemporal();
                    start = auth.getBulb2StartTime();
                    end = auth.getBulb2EndTime();
                } else { //STRIP
                    onOffIsChecked[0] = auth.isLedStripOnOff();
                    brightnessIsChecked[0] = auth.isLedStripBrightness();
                    isSupervisedIsChecked[0] = auth.isLedStripIsSupervised();
                    supervisedByName = auth.getLedStripSupervisedBy();
                    isTemporalIsChecked[0] = auth.isLedStripIsTemporal();
                    start = auth.getLedStripStartTime();
                    end = auth.getLedStripEndTime();
                }

                if (isSupervisedIsChecked[0]) {
                    int index = 0;
                    for (int j = 0; j < authListFromServer.size(); j++) {
                        if (supervisedByName.equals(authListFromServer.get(j).getGroupID())) {
                            index = j;
                        }
                    }//그룹이 속한 index 찾기

                    stripSupervisedPermissionSwitch.setChecked(true);
                    supervisedSettingRableRow.setVisibility(View.VISIBLE);
                    supervisedGroupSelecterSpinner.setSelection(index);
                } else {
                    stripSupervisedPermissionSwitch.setChecked(false);
                    supervisedSettingRableRow.setVisibility(View.GONE);
                } //set supervised permission


                if (isTemporalIsChecked[0]) {
                    temporalPermissionSwitch.setChecked(true);

                    startTimeTextEdit = (EditText) findViewById(R.id.strip_start_time);
                    startTimeTextEdit.setText(start);

                    endTimeTextEdit = (EditText) findViewById(R.id.strip_end_time);
                    endTimeTextEdit.setText(end);

                    int parsingChar = start.indexOf(":");
                    startTimePicker.setHour(Integer.parseInt(start.substring(0, parsingChar).trim()));
                    startTimePicker.setMinute(Integer.parseInt(start.substring(parsingChar + 1).trim()));

                    parsingChar = end.indexOf(":");
                    endTimePicker.setHour(Integer.parseInt(end.substring(0, parsingChar).trim()));
                    endTimePicker.setMinute(Integer.parseInt(end.substring(parsingChar + 1).trim()));
                } else {
                    temporalPermissionSwitch.setChecked(false);
                }
                break;
            }
        }


        onOffButton[0].setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                if (onOffIsChecked[0]) {
                    onOffIsChecked[0] = false;
                } else {
                    onOffIsChecked[0] = true;
                }
                //toggle

                if (onOffIsChecked[0] == true) {
                    if (brightnessIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+BOTH);
                        backroundImage.setImageResource(R.drawable.bulb_both);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+ONLY_ON_OFF);
                        backroundImage.setImageResource(R.drawable.bulb_onoff);
                    }
                } else {
                    if (brightnessIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+ONLY_BRIGHTNESS);
                        backroundImage.setImageResource(R.drawable.bulb_brightness);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+NOTHING);
                        backroundImage.setImageResource(R.drawable.bulb_specific);
                    }
                }

            }
        });


        ledStripBrightnessButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                if (brightnessIsChecked[0] == true) {
                    brightnessIsChecked[0] = false;
                } else {
                    brightnessIsChecked[0] = true;
                }

                if (brightnessIsChecked[0] == true) {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+BOTH);
                        backroundImage.setImageResource(R.drawable.bulb_both);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+ONLY_BRIGHTNESS);
                        backroundImage.setImageResource(R.drawable.bulb_brightness);
                    }
                } else {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+ONLY_ON_OFF);
                        backroundImage.setImageResource(R.drawable.bulb_onoff);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+NOTHING);
                        backroundImage.setImageResource(R.drawable.bulb_specific);
                    }
                }
            }
        });

        ledStripBrightnessButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                if (brightnessIsChecked[0] == true) {
                    brightnessIsChecked[0] = false;
                } else {
                    brightnessIsChecked[0] = true;
                }

                if (brightnessIsChecked[0] == true) {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+BOTH);
                        backroundImage.setImageResource(R.drawable.bulb_both);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+ONLY_BRIGHTNESS);
                        backroundImage.setImageResource(R.drawable.bulb_brightness);
                    }
                } else {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+ONLY_ON_OFF);
                        backroundImage.setImageResource(R.drawable.bulb_onoff);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+NOTHING);
                        backroundImage.setImageResource(R.drawable.bulb_specific);
                    }
                }
            }
        });

        ledStripBrightnessButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                if (brightnessIsChecked[0] == true) {
                    brightnessIsChecked[0] = false;
                } else {
                    brightnessIsChecked[0] = true;
                }

                if (brightnessIsChecked[0] == true) {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+BOTH);
                        backroundImage.setImageResource(R.drawable.bulb_both);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+ONLY_BRIGHTNESS);
                        backroundImage.setImageResource(R.drawable.bulb_brightness);
                    }
                } else {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+ONLY_ON_OFF);
                        backroundImage.setImageResource(R.drawable.bulb_onoff);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+NOTHING);
                        backroundImage.setImageResource(R.drawable.bulb_specific);
                    }
                }
            }
        });

        ledStripBrightnessButton4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                if (brightnessIsChecked[0] == true) {
                    brightnessIsChecked[0] = false;
                } else {
                    brightnessIsChecked[0] = true;
                }

                if (brightnessIsChecked[0] == true) {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+BOTH);
                        backroundImage.setImageResource(R.drawable.bulb_both);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+ONLY_BRIGHTNESS);
                        backroundImage.setImageResource(R.drawable.bulb_brightness);
                    }
                } else {
                    if (onOffIsChecked[0] == true) {
                        toggleView.setText(selectedDevice+"\n"+ONLY_ON_OFF);
                        backroundImage.setImageResource(R.drawable.bulb_onoff);
                    } else {
                        toggleView.setText(selectedDevice+"\n"+NOTHING);
                        backroundImage.setImageResource(R.drawable.bulb_specific);
                    }
                }
            }
        });


        if (stripSupervisedPermissionSwitch.isChecked()) {
            supervisedSettingRableRow.setVisibility(View.VISIBLE);
        } else {
            supervisedSettingRableRow.setVisibility(View.GONE);
        }

        stripSupervisedPermissionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    supervisedSettingRableRow.setVisibility(View.VISIBLE);
                } else {
                    supervisedSettingRableRow.setVisibility(View.GONE);
                }
            }
        });


        if (temporalPermissionSwitch.isChecked()) {
            temporalSettingTableRow1.setVisibility(View.VISIBLE);
            temporalSettingTableRow2.setVisibility(View.VISIBLE);
            timePickerWapper.setVisibility(View.VISIBLE);

        } else {
            temporalSettingTableRow1.setVisibility(View.INVISIBLE);
            temporalSettingTableRow2.setVisibility(View.INVISIBLE);
            timePickerWapper.setVisibility(View.INVISIBLE);
        }

        temporalPermissionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    temporalSettingTableRow1.setVisibility(View.VISIBLE);
                    temporalSettingTableRow2.setVisibility(View.VISIBLE);
                    timePickerWapper.setVisibility(View.VISIBLE);

                } else {
                    temporalSettingTableRow1.setVisibility(View.GONE);
                    temporalSettingTableRow2.setVisibility(View.GONE);
                    timePickerWapper.setVisibility(View.GONE);

                }
            }
        });

        startTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                EditText startTimeTextEdit = (EditText) findViewById(R.id.strip_start_time);
                startTimeTextEdit.setText(hourOfDay + " : " + minute);
            }
        });


        endTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                EditText endTimeTextEdit = (EditText) findViewById(R.id.strip_end_time);
                endTimeTextEdit.setText(hourOfDay + " : " + minute);
            }
        });


        Button saveButton = (Button) findViewById(R.id.strip_detail_save_button);
        EditText finalStartTimeTextEdit = startTimeTextEdit;
        EditText finalEndTimeTextEdit = endTimeTextEdit;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SAVE", "Save something");
                new AlertDialog.Builder(GoogleHome_LED_Strip.this)
                        .setTitle("[SAVE AUTHORITY]")
                        .setMessage("If you click yes, this authrority information will be saved.\ncontinue?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) { //SAVE EXECUTION
                                Toast.makeText(getApplicationContext(), "SAVE AUTH INFO COMPLETE", Toast.LENGTH_SHORT).show();

                                //DO Update
                                String groupNameForUpdate = groupSelectSpinner.getSelectedItem().toString();
                                int index = groupSelectSpinner.getSelectedItemPosition();

                                Auth newAuth = new Auth(groupNameForUpdate);
                                //group의 index랑 해당 device의 정보만 채워서 보낸다.


                                if (selectedDevice.equals(BULB_1)) { //Bulb1
                                    if (onOffIsChecked[0] | brightnessIsChecked[0])
                                        newAuth.setBulb1(true);
                                    else newAuth.setBulb1(false);
                                    newAuth.setBulb1OnOff(onOffIsChecked[0]);
                                    newAuth.setBulb1Brightness(brightnessIsChecked[0]);
                                    newAuth.setBulb1IsSupervised(stripSupervisedPermissionSwitch.isChecked());
                                    if (!stripSupervisedPermissionSwitch.isChecked()) {
                                        newAuth.setBulb1SupervisedBy("None");
                                    } else {
                                        newAuth.setBulb1SupervisedBy(supervisedGroupSelecterSpinner.getSelectedItem().toString());
                                    }
                                    newAuth.setBulb1IsTemporal(temporalPermissionSwitch.isChecked());
                                    newAuth.setBulb1StartTime(finalStartTimeTextEdit.getText().toString());
                                    newAuth.setBulb1EndTime(finalEndTimeTextEdit.getText().toString());
                                    try {
                                        jp.updateConfigFile_V2_Bulb1(index, newAuth);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                else if(selectedDevice.equals(BULB_2)){
                                    if(onOffIsChecked[0] | brightnessIsChecked[0])
                                        newAuth.setBulb2(true);
                                    else newAuth.setBulb2(false);

                                    newAuth.setBulb2OnOff(onOffIsChecked[0]);
                                    newAuth.setBulb2Brightness(brightnessIsChecked[0]);

                                    newAuth.setBulb2IsSupervised(stripSupervisedPermissionSwitch.isChecked());
                                    if (!stripSupervisedPermissionSwitch.isChecked()) {
                                        newAuth.setBulb2SupervisedBy("None");
                                    } else {
                                        newAuth.setBulb2SupervisedBy(supervisedGroupSelecterSpinner.getSelectedItem().toString());
                                    }
                                    newAuth.setBulb2IsTemporal(temporalPermissionSwitch.isChecked());
                                    newAuth.setBulb2StartTime(finalStartTimeTextEdit.getText().toString());
                                    newAuth.setBulb2EndTime(finalEndTimeTextEdit.getText().toString());
                                    try {
                                        jp.updateConfigFile_V2_Bulb2(index, newAuth);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                else if(selectedDevice.equals(STRIP)){
                                    if(onOffIsChecked[0] | brightnessIsChecked[0])
                                        newAuth.setLedStrip(true);
                                    else newAuth.setLedStrip(false);

                                    newAuth.setLedStripOnOff(onOffIsChecked[0]);
                                    newAuth.setLedStripBrightness(brightnessIsChecked[0]);

                                    newAuth.setLedStripIsSupervised(stripSupervisedPermissionSwitch.isChecked());
                                    if (!stripSupervisedPermissionSwitch.isChecked()) {
                                        newAuth.setLedStripSupervisedBy("None");
                                    } else {
                                        newAuth.setLedStripSupervisedBy(supervisedGroupSelecterSpinner.getSelectedItem().toString());
                                    }
                                    newAuth.setLedStripIsTemporal(temporalPermissionSwitch.isChecked());
                                    newAuth.setLedStripStartTime(finalStartTimeTextEdit.getText().toString());
                                    newAuth.setLedStripEndTime(finalEndTimeTextEdit.getText().toString());
                                    try {
                                        jp.updateConfigFile_V2_LED_STRIP(index, newAuth);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
