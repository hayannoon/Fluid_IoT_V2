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
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ManageConfiguration extends Activity implements Serializable {
    final static String MANAGE_CONFIG_DEBUGGING_TAG = "IOT_MANAGE_CONFIGURATION";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeManageConfig();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeManageConfig();
    }



    private void initializeManageConfig(){
        setContentView(R.layout.manage_configuration);

        //이전에 jsonParser에서 json을 읽고, auth 배열을 반환해주면 그걸 활용함.

        JsonParser jp = new JsonParser();
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

        List<String> groupIDList = new ArrayList<>();
        for(Auth auth: authList){
            groupIDList.add(auth.getGroupID());
        }


        if(authList!= null){
            ListView listView = findViewById(R.id.auth_listview);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,groupIDList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    Intent intent = new Intent(getApplicationContext(), ManageConfigurationDetail.class);
                    intent.putExtra("SELECTED_GROUP", position);
                    startActivity(intent);

                }
            });
        }

        Button createNewGroupButton = (Button) findViewById(R.id.create_new_group_button);
        createNewGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(MANAGE_CONFIG_DEBUGGING_TAG, "create new group!!!");
                Intent intent = new Intent(getApplicationContext(), CreateGroup.class);
                startActivity(intent);

            }
        });

    }


}
