package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GoogleHome_LED_Strip extends Activity implements AdapterView.OnItemSelectedListener {

    String[] items;
    String[] items1;
    Spinner groupSelectSpinner;
    Spinner supervisedGroupSelecterSpinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_home_led_strip);

        JsonParser jp = new JsonParser();
        List<Auth> authList = null;

        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }//authList에는 json을 파싱해서 클래스 배열로 만든 결과가 담겨있음

        List<String> groupIDList = new ArrayList<>();
        for (Auth auth : authList) {
            groupIDList.add(auth.getGroupID());
        } //여기까지 오면 GroupIDList 완성
        items = (String[]) groupIDList.toArray(new String[0]); // List to Array

        groupSelectSpinner = (Spinner) findViewById(R.id.led_strip_group_select_spinner);
        //items = new String[]{"Choose Group", "Jacket", "Top", "Bottom", "Accs"};
        groupSelectSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSelectSpinner.setAdapter(adapter);

        items1 = (String[]) groupIDList.toArray(new String[0]); // List to Array


        supervisedGroupSelecterSpinner = (Spinner) findViewById(R.id.led_strip_supervised_group_select_spinner);
        supervisedGroupSelecterSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supervisedGroupSelecterSpinner.setAdapter(adapter1);



        Button onOffButton = (Button) findViewById(R.id.led_on_off_button);
        onOffButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                toggleView.setText("ON/OFF control");
            }
        });

        Button ledStripBrightnessButton1 = (Button) findViewById(R.id.led_strip_brightness_button1);
        Button ledStripBrightnessButton2 = (Button) findViewById(R.id.led_strip_brightness_button2);
        Button ledStripBrightnessButton3 = (Button) findViewById(R.id.led_strip_brightness_button3);
        Button ledStripBrightnessButton4 = (Button) findViewById(R.id.led_strip_brightness_button4);

        ledStripBrightnessButton1.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                toggleView.setText("Brightness control");
            }
        });

        ledStripBrightnessButton2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                toggleView.setText("Brightness control");
            }
        });

        ledStripBrightnessButton3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                toggleView.setText("Brightness control");
            }
        });

        ledStripBrightnessButton4.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TextView toggleView = (TextView) findViewById(R.id.led_strip_toggle_textview);
                toggleView.setText("Brightness control");
            }
        });



        TableRow supervisedSettingRableRow = (TableRow) findViewById(R.id.strip_supervisedSetting);
        Switch stripSupervisedPermissionSwitch = (Switch) findViewById(R.id.strip_supervised_permission_switch);

        if(stripSupervisedPermissionSwitch.isChecked()){
            supervisedSettingRableRow.setVisibility(View.VISIBLE);
        }else{
            supervisedSettingRableRow.setVisibility(View.GONE);

        }

        stripSupervisedPermissionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    supervisedSettingRableRow.setVisibility(View.VISIBLE);
                }else{
                    supervisedSettingRableRow.setVisibility(View.GONE);
                }
            }
        });

        View container = findViewById(R.id.strip_supervisedSetting);
        container.animate().setDuration(300).alpha(0.0f).withEndAction(new Runnable() {
            @Override
            public void run() {
                container.setVisibility(View.GONE);
                container.setAlpha(1.0f);
            }
        });


        LinearLayout timePickerWapper = (LinearLayout) findViewById(R.id.strip_time_picker_wapper_layout);

        TableRow temporalSettingTableRow1 = (TableRow) findViewById(R.id.strip_start_time_wrapper);
        TableRow temporalSettingTableRow2 = (TableRow) findViewById(R.id.strip_end_time_wrapper);
        TimePicker startTimePicker = (TimePicker) findViewById(R.id.strip_start_time_picker);
        TimePicker endTimePicker = (TimePicker) findViewById(R.id.strip_end_time_picker);


        Switch temporalPermissionSwitch = (Switch) findViewById(R.id.temporal_permission_switch);

        if(temporalPermissionSwitch.isChecked()){
            temporalSettingTableRow1.setVisibility(View.VISIBLE);
            temporalSettingTableRow2.setVisibility(View.VISIBLE);
            timePickerWapper.setVisibility(View.VISIBLE);

        }else{
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

                }else{
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
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d("SAVE", "Save something");
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
