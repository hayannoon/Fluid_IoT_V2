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


public class DebuggingActivity extends AppCompatActivity {

    final static String DEBUGGINGACTIVITY_TAG = "IOT_DEBUGGING_ACTIVITY_TAG";
    private TextView resultTextView ;


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

                String url= "http://13.125.172.116:8080/iot_auth/account.json";


                String inputID = ((EditText) findViewById(R.id.debug_id_input)).getText().toString();
                String inputPW = ((EditText) findViewById(R.id.debug_pw_input)).getText().toString();

                Log.d(DEBUGGINGACTIVITY_TAG, "input ID : " + inputID + ", input PW : " + inputPW);

                //여기서 ID/PW정보로 Group 정보를 가져오고, Group정보에서 True로 지정되어있는 기기의 UI값을 UI_info.json파일을 참조해서 가져온다.
                // AsyncTask를 통해 HttpURLConnection 수행.
                NetworkConnectionForRead networkTask = new NetworkConnectionForRead(url, null);
                networkTask.execute();


            }
        });

        Button debugPostButton = (Button) findViewById(R.id.debug_post_button);
        debugPostButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d(DEBUGGINGACTIVITY_TAG, "post btn clicked");

                String inputID = ((EditText) findViewById(R.id.debug_id_input)).getText().toString();
                String inputPW = ((EditText) findViewById(R.id.debug_pw_input)).getText().toString();
                String jsonContents = "{\"Accounts\" : [{\"ID\":\"" + inputID + "\", \"PW\":\"" + inputPW + "\"}]}";

                NetworkConnectForWrite net = new NetworkConnectForWrite("account.json", jsonContents);
                net.start();
            }
        });

    }


    class NetworkConnectForWrite extends Thread{ //서버에 파일을 Write할때 사용한다.

        private String fileContents;
        private String serverURL = "http://13.125.172.116:8080/iot_auth/";

        public NetworkConnectForWrite(String fileName, String jsonContents){
            this.fileContents = jsonContents;
            this.serverURL = this.serverURL.concat(fileName);
        }

        @Override
        public void run(){
            try {
                // Create an HTTP client instance
                URL url = new URL(this.serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Open a connection to the server
                conn.connect();

                // Write the JSON data to the output stream
                OutputStream outputStream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(this.fileContents); //내용을 여기에 넣어주면 된다.

                writer.flush();
                writer.close();
                outputStream.close();

                // Get the response code from the server
                int responseCode = conn.getResponseCode();
                Log.d(DEBUGGINGACTIVITY_TAG,conn.getContent().toString());
                if (responseCode == HttpURLConnection.HTTP_OK ||  responseCode==HttpURLConnection.HTTP_NO_CONTENT) {
                    Log.d(DEBUGGINGACTIVITY_TAG, "UPLOAD_SERVER_SUCCESS");
                    // File uploaded successfully
                } else {
                    // File upload failed
                    Log.d(DEBUGGINGACTIVITY_TAG, "UPLOAD_SERVER_FAILED " + responseCode);

                }
            } catch (Exception e) {
                // Handle exception
                Log.e("[HTTP_ERROR]", e.getMessage(), e);
            }
        }
    }


    class NetworkConnectionForRead extends AsyncTask<Void, Void, String> {

        private String url;
        private ContentValues values;

        public NetworkConnectionForRead(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            //내용이 s에 저장된다. 이 s를 json파일에 저장해주면 될듯하다.
            resultTextView.setText(s);
        }
    }


}
