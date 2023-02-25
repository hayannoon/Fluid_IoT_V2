package com.example.iot_master_prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.FileNotFoundException;


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


        Button deleteIdButton = (Button) findViewById(R.id.manage_account); //delete id button event
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

        Button resetConfigButton = (Button) findViewById(R.id.reset_configuration); //reset configuration button event
        resetConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // default configuration
                JsonParser jp = new JsonParser();
                jp.writeConfigFile(JsonParser.AUTH_CONFIGURATION_FILE, JsonParser.DEFAULT_CONFIG_STRING, getApplicationContext());
                jp.writeConfigFile(JsonParser.ACCOUNT_FILE, JsonParser.DEFAULT_ACCOUNT_STRING, getApplicationContext());
            }
        });

        Button forDebuggingButton = (Button) findViewById(R.id.for_debugging);
        forDebuggingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonParser jp = new JsonParser();
                try {
                    Log.d(DEBUGGING_TAG, "for debugging button click event occur!!!");
                    jp.changeGroupName("master", "new master", getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });







    }


}