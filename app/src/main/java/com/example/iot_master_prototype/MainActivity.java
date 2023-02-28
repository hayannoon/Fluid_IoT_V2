package com.example.iot_master_prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.io.FileNotFoundException;


public class MainActivity extends AppCompatActivity {

    final static String DEBUGGING_TAG = "IOT_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button manageIdButton = (Button) findViewById(R.id.manage_account); //delete id button event
        manageIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Log.d(DEBUGGING_TAG, "manage ID button clicked!");

                Intent intent = new Intent(getApplicationContext(), ManageId.class);
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
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("[RESET CONFIGURATION]")
                        .setMessage("If you click yes, every account and group information will be deleted. \n continue?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //finish();
                                JsonParser jp = new JsonParser();
                                jp.writeConfigFile(JsonParser.AUTH_CONFIGURATION_FILE, JsonParser.DEFAULT_CONFIG_STRING, getApplicationContext());
                                jp.writeConfigFile(JsonParser.ACCOUNT_FILE, JsonParser.DEFAULT_ACCOUNT_STRING, getApplicationContext());
                                Toast.makeText(getApplicationContext(), "RESET CONFIGURATION COMPLETE", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "RESET CONFIGURATION CANCEL", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .create().show();

                JsonParser jp = new JsonParser();
                jp.writeConfigFile(JsonParser.AUTH_CONFIGURATION_FILE, JsonParser.DEFAULT_CONFIG_STRING, getApplicationContext());
                jp.writeConfigFile(JsonParser.ACCOUNT_FILE, JsonParser.DEFAULT_ACCOUNT_STRING, getApplicationContext());
            }
        });

        Button forDebuggingButton = (Button) findViewById(R.id.for_debugging);
        forDebuggingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(DEBUGGING_TAG, "for debugging button click event occur!!!");

            }
        });






    }


}