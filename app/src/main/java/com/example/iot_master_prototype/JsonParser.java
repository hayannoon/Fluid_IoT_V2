package com.example.iot_master_prototype;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class JsonParser {

    final static String JSONPARSER_DEBUGGING_TAG = "IOT_JsonParser";

    Context context;
    public JsonParser(Context context){
        this.context = context;
    }

    public JsonParser() {

    }

    void jsonParsing(String json)
    {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter the jsonParsing function");
        try{
            JSONObject jsonObject = new JSONObject(json);

            JSONArray authArray = jsonObject.getJSONArray("groups");

            for(int i=0; i<authArray.length(); i++)
            {
                JSONObject authObject = authArray.getJSONObject(i);

                Log.d(JSONPARSER_DEBUGGING_TAG, authObject.getString("name"));
                Log.d(JSONPARSER_DEBUGGING_TAG, authObject.getString("auth"));

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }




      String getJsonString(String fileName){

         String json = "";

         try {

             InputStream is = context.getAssets().open(fileName);
             int fileSize = is.available();

             byte[] buffer = new byte[fileSize];
             is.read(buffer);
             is.close();

             json = new String(buffer, "UTF-8");
         }
         catch (IOException ex)
         {
             ex.printStackTrace();
         }

         return json;
     }

     boolean changeGroupName(String before, String after) throws JSONException {
        String jsonData = this.getJsonString("configuration.json");
         JSONObject jsonObject = new JSONObject(jsonData);

         JSONArray authArray = jsonObject.getJSONArray("groups");

         for(int i=0; i<authArray.length(); i++)
         {
             JSONObject authObject = authArray.getJSONObject(i);
             if(authObject.getString("name").equals(before)){
                 authObject.put("name", after);
                 authObject.put("test", "testing");

                 Log.d(JSONPARSER_DEBUGGING_TAG, "changed group name from " + before +" after " + after);
             }
         } //jsonObject는 새롭게 변경됨. 이걸 다시 String으로 변경해서 파일에 써야함.

         jsonData = jsonObject.toString(); //수정된 json의 String 타입 value가 저장.



         return true;
     }



}
