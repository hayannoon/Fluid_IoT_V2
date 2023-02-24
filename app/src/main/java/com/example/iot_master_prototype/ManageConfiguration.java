package com.example.iot_master_prototype;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManageConfiguration extends Activity implements Serializable {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_configuration);

        //이전에 jsonParser에서 json을 읽고, auth 배열을 반환해주면 그걸 활용함.

        JsonParser jp = new JsonParser();
        List<Auth> authList = null;
        try {
            authList = jp.getAuthListFromConfigFile(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } //authList에는 json을 파싱해서 클래스 배열로 만든 결과가 담겨있음

        List<String> groupIDList = new ArrayList<>();
        for(Auth auth: authList){
            groupIDList.add(auth.getGroupID());
        }


        if(authList!= null){
            final TextView groupIDTextView = findViewById(R.id.group_id_textview);
            ListView listView = findViewById(R.id.auth_listview);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,groupIDList);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    //String data = (String) adapterView.getItemAtPosition(position);
                    //groupIDTextView.setText(data);

                    Intent intent = new Intent(getApplicationContext(), ManageConfigurationDetail.class);
                    intent.putExtra("SELECTED_GROUP", position);
                    startActivity(intent);

                }
            });
        }



    }
}
