package com.example.iot_master_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageId extends Activity implements Serializable {

    final static String MANAGE_ID_DEBUGGING_TAG = "IOT_MANAGE_ID";

    private void initializeManageId(){
        setContentView(R.layout.manage_id);

        JsonParser jp = new JsonParser();
        List<Account> accountList = null;

        try {
            accountList = jp.getAccountListFromAccountFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } //accountList에는 json을 파싱해서 클래스 배열로 만든 결과가 담겨있음

        List<String> userIDList = new ArrayList<>();
        for (Account account : accountList) {
            userIDList.add(account.getUserID());
        }

        if (accountList != null) {
            ListView listView = findViewById(R.id.account_listview);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userIDList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(getApplicationContext(), ManageIdDetail.class);
                    intent.putExtra("SELECTED_ACCOUNT", position);
                    startActivity(intent);
                    Log.d(MANAGE_ID_DEBUGGING_TAG, "On ITEM CLICKED!");
                }
            });
        }

        Button createNewAccountButton = (Button) findViewById(R.id.create_new_account_button);
        createNewAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d(MANAGE_ID_DEBUGGING_TAG, "create new account button clicked!!!");
                Intent intent = new Intent(getApplicationContext(), CreateId.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeManageId();
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.initializeManageId();

    }


}
