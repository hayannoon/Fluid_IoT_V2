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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        JsonParser jp = new JsonParser();

        Button debugGetUIListButton = (Button) findViewById(R.id.debug_ui_list_request_btn);
        debugGetUIListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = "http://13.125.172.116:8080/iot_auth/account.json";

                String inputID = ((EditText) findViewById(R.id.debug_id_input)).getText().toString();
                String inputPW = ((EditText) findViewById(R.id.debug_pw_input)).getText().toString();
                Log.d(DEBUGGINGACTIVITY_TAG, "input ID : " + inputID + ", input PW : " + inputPW);


                //Do Something

                //1. Get Account File from Server


                //2. Find matching ID

                //3. If there is not matched ID => return false

                //4. else => Get Group ID then take the list of true device

                //5. Get Devices_info file then return the {UI-ID, text} list

                String returnValue = "BEFORE";

                resultTextView.setText("Something");
                try {
                    returnValue = jp.getUIInfoFromAccount(inputID, inputPW);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultTextView.setText(returnValue);

                try {
                    jp.getAuthListFromCOnfigFile_v2();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
