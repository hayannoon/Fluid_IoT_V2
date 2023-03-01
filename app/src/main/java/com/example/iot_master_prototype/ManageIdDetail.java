package com.example.iot_master_prototype;

import android.app.Activity;
import android.content.Intent;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManageIdDetail extends Activity implements Serializable, AdapterView.OnItemSelectedListener {
    final static String MangeIdDetail_DEBUGGING_TAG = "IOT_ManageIdDetail";

    String[] items;
    EditText userID;
    EditText userPW;
    Spinner groupSelectSpinner;
    Toast t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_id_detail);

        Intent intent = getIntent();
        int position = (int) intent.getSerializableExtra("SELECTED_ACCOUNT");

        JsonParser jp = new JsonParser(ManageIdDetail.this);
        List<Account> accountList = null;
        try {
            accountList = jp.getAccountListFromAccountFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } //authList에는 account.json을 파싱해서 클래스 배열로 만든 결과가 담겨있음

        List<Auth> authList = null;
        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } //authList에는 configuration.json을 파싱해서 클래스 배열로 만든 결과가 담겨있음
        catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> groupIDList = new ArrayList<>();
        for (Auth auth : authList) {
            groupIDList.add(auth.getGroupID());
        } //여기까지 오면 GroupIDList 완성
        items = (String[]) groupIDList.toArray(new String[0]); // List to Array

        int selectedGroupIndex = 0; //현재 유저가 속한 group의 인덱스를 찾는 과정
        String selectedGroupId = accountList.get(position).getGroup();
        for (int i = 0; i < groupIDList.size(); i++) {
            if (groupIDList.get(i).equals(selectedGroupId)) {
                selectedGroupIndex = i;
            }
        }

        groupSelectSpinner = (Spinner) findViewById(R.id.group_select_spinner_detail);
        groupSelectSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSelectSpinner.setAdapter(adapter);


        Account selectedAccoutObject = accountList.get(position); // 유저가 선택한 account로 진입

        if (selectedAccoutObject != null) {
            userID = (EditText) findViewById(R.id.editText_ID_detail);
            userPW = (EditText) findViewById(R.id.editText_PW_detail);
            userID.setText(selectedAccoutObject.getUserID());
            userPW.setText(selectedAccoutObject.getUserPW());
            groupSelectSpinner.setSelection(selectedGroupIndex);
        } //set id/pw/group name


        Button updateAccountButton = (Button) findViewById(R.id.account_update_button);
        updateAccountButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(MangeIdDetail_DEBUGGING_TAG, "UPDATE ACCOUNT BUTTON CLICKED!!!");

                Account newAccount = new Account(userID.getText().toString(), userPW.getText().toString(), groupSelectSpinner.getSelectedItem().toString());
                try {
                    if (jp.updateAccountFile(position, newAccount, getApplicationContext())) {
                        t = Toast.makeText(getApplicationContext(), "Update Account Success!", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        t.show();
                    } else {
                        t = Toast.makeText(getApplicationContext(), "Update Account Failed", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
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

        Button deleteAccountButton = (Button) findViewById(R.id.account_delete_button);
        deleteAccountButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.d(MangeIdDetail_DEBUGGING_TAG, "DELETE ACCOUNT BUTTON CLICKED!!!");

                try {
                    if(jp.removeAccount(position,getApplicationContext())){
                        t = Toast.makeText(getApplicationContext(), "Delete Account Success!", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER_VERTICAL,0,0);
                        t.show();
                    }else{
                        t = Toast.makeText(getApplicationContext(), "Delete Account Failed", Toast.LENGTH_LONG);
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
        return;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        return;
    }
}
