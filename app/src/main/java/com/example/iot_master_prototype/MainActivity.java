package com.example.iot_master_prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {

    final static String DEBUGGING_TAG = "IOT_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button createGroupButton = (Button) findViewById(R.id.create_group); //Create group Button Event
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View view){
                    Log.d(DEBUGGING_TAG, "create group button clicked!");

                    Intent intent = new Intent(getApplicationContext(), CreateGroup.class);
                    startActivity(intent);
                }
        });

        Button createIdButton = (Button) findViewById(R.id.create_id); //Create ID button event
        createIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d(DEBUGGING_TAG, "create ID button clicked!");

                Intent intent = new Intent(getApplicationContext(), CreateId.class);
                startActivity(intent);
            }
        });

        Button deleteGroupButton = (Button) findViewById(R.id.delete_group); //delete group button event
        deleteGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d(DEBUGGING_TAG, "delete Group button clicked!");

                Intent intent = new Intent(getApplicationContext(), DeleteGroup.class);
                startActivity(intent);
            }
        });

        Button deleteIdButton = (Button) findViewById(R.id.delete_id); //delete id button event
        deleteIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d(DEBUGGING_TAG, "delete ID button clicked!");

                Intent intent = new Intent(getApplicationContext(), DeleteId.class);
                startActivity(intent);
            }
        });



        Button manageConfigButton = (Button) findViewById(R.id.manage_configuration); //manage configuration button event
        manageConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d(DEBUGGING_TAG, "manage configuration button clicked!");

                Intent intent = new Intent(getApplicationContext(), ManageConfiguration.class);
                startActivity(intent);
            }
        });







    }


}