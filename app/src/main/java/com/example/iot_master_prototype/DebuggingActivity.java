package com.example.iot_master_prototype;

import android.app.Activity;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class DebuggingActivity extends AppCompatActivity {

    final static String DEBUGGINGACTIVITY_TAG = "IOT_DEBUGGING_ACTIVITY_TAG";
    private TextView resultTextView;
    private String tmp = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debugging_activity);

        resultTextView = (TextView) findViewById(R.id.debug_ui_list_result_textview);
        resultTextView.setText("TEST INFO");

        Button debugGetUIListButton = (Button) findViewById(R.id.debug_ui_list_request_btn);
        debugGetUIListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://13.125.172.116:8080/iot_auth/account.json";

                String inputID = ((EditText) findViewById(R.id.debug_id_input)).getText().toString();
                String inputPW = ((EditText) findViewById(R.id.debug_pw_input)).getText().toString();
                Log.d(DEBUGGINGACTIVITY_TAG, "input ID : " + inputID + ", input PW : " + inputPW);

                //Do Something

                resultTextView.setText("Something");

                //Something에는 ID/PW가 가지고 있는 권한에 해당하는 UI 목록을 보여줘야한다.

            }
        });

        Button debugPostButton = (Button) findViewById(R.id.debug_post_button);
        debugPostButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(DEBUGGINGACTIVITY_TAG, "post btn clicked");
            }
        });

    }
}
