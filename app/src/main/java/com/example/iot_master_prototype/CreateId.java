package com.example.iot_master_prototype;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CreateId extends Activity implements AdapterView.OnItemSelectedListener {

    String[] items;
    EditText userID;
    EditText userPW;
    Spinner groupSelectSpinner;
    Toast t;

    final static String CREATE_ID_DEBUGGING_TAG = "IOT_CREATE_ID";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_id);


        // ComboBox를 위한 spinner, json읽어와서 GroupName을 맵핑해준다.

        JsonParser jp = new JsonParser();
        List<Auth> authList = null;
        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } //authList에는 json을 파싱해서 클래스 배열로 만든 결과가 담겨있음

        List<String> groupIDList = new ArrayList<>();
        for (Auth auth : authList) {
            groupIDList.add(auth.getGroupID());
        } //여기까지 오면 GroupIDList 완성
        items = (String[]) groupIDList.toArray(new String[0]); // List to Array


        groupSelectSpinner = (Spinner) findViewById(R.id.group_select_spinner);
        //items = new String[]{"Choose Group", "Jacket", "Top", "Bottom", "Accs"};
        groupSelectSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSelectSpinner.setAdapter(adapter);


        Button createIdSendbutton = (Button) findViewById(R.id.create_id_send_btn);
        createIdSendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Id, PW, Group 저장해서 json으로 account.json에 저장
                userID = (EditText) findViewById(R.id.editText_ID);
                userPW = (EditText) findViewById(R.id.editText_PW);
                groupSelectSpinner = (Spinner) findViewById(R.id.group_select_spinner);

                Log.d(CREATE_ID_DEBUGGING_TAG, userID.getText().toString() + " " + userPW.getText().toString() + " " + groupSelectSpinner.getSelectedItem().toString());

                Account newAccount = new Account(userID.getText().toString(), userPW.getText().toString(), groupSelectSpinner.getSelectedItem().toString());
                try {
                    if(jp.addAccountFile(newAccount, getApplicationContext() )){
                        t = Toast.makeText(getApplicationContext(), "Create Account Success!", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        t.show();} else{
                        t = Toast.makeText(getApplicationContext(), "[FAILED] DUPLICATE ID", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        t.show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //
    }
}
