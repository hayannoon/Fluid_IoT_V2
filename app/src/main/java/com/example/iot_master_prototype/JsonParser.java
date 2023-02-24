package com.example.iot_master_prototype;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonParser {

    final static String JSONPARSER_DEBUGGING_TAG = "IOT_JsonParser";
    final static String AUTH_CONFIGURATION_FILE = "configuration.json";
    final static String DEFAULT_CONFIG_STRING = "{\n" +
            "  \"groups\": [\n" +
            "    {\n" +
            "      \"group_name\" : \"master\",\n" +
            "      \"auth\" : {\n" +
            "        \"bulb1\" : \"true\",\n" +
            "        \"bulb2\" : \"true\",\n" +
            "        \"strip\" : \"true\",\n" +
            "        \"camera\" : \"true\",\n" +
            "        \"speaker\" : \"true\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"group_name\" : \"onlyBulb\",\n" +
            "      \"auth\" : {\n" +
            "        \"bulb1\" : \"true\",\n" +
            "        \"bulb2\" : \"true\",\n" +
            "        \"strip\" : \"true\",\n" +
            "        \"camera\" : \"false\",\n" +
            "        \"speaker\" : \"false\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"group_name\" : \"onlyCamera\",\n" +
            "      \"auth\" : {\n" +
            "        \"bulb1\" : \"false\",\n" +
            "        \"bulb2\" : \"false\",\n" +
            "        \"strip\" : \"false\",\n" +
            "        \"camera\" : \"true\",\n" +
            "        \"speaker\" : \"false\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"group_name\" : \"onlySpeaker\",\n" +
            "      \"auth\" : {\n" +
            "        \"bulb1\" : \"false\",\n" +
            "        \"bulb2\" : \"false\",\n" +
            "        \"strip\" : \"false\",\n" +
            "        \"camera\" : \"false\",\n" +
            "        \"speaker\" : \"true\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    Context context;

    public JsonParser(Context context) {
        this.context = context;
    }

    public JsonParser() {

    }

    void jsonParsing(String json) throws FileNotFoundException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter the jsonParsing function");
        Log.d(JSONPARSER_DEBUGGING_TAG, "Original Data : " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray authArray = jsonObject.getJSONArray("groups");

            for (int i = 0; i < authArray.length(); i++) {
                JSONObject authObject = authArray.getJSONObject(i);
                Log.d(JSONPARSER_DEBUGGING_TAG, "Count : " + i);

                Log.d(JSONPARSER_DEBUGGING_TAG, authObject.getString("name"));
                Log.d(JSONPARSER_DEBUGGING_TAG, authObject.getString("auth"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(JSONPARSER_DEBUGGING_TAG, "EXIT the json parsing function : ");
    }

    String getJsonString(String fileName, Context context) throws FileNotFoundException { //input = filename, output = json Data(String type)
        FileInputStream fis = context.openFileInput(fileName);
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            Log.d("getjsonString_IOT", stringBuilder.toString());
            return stringBuilder.toString();
        }
    }


    boolean changeGroupName(String before, String after, Context context) throws JSONException, FileNotFoundException {
        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance

        JSONArray authArray = jsonObject.getJSONArray("groups");

        for (int i = 0; i < authArray.length(); i++) {
            JSONObject authObject = authArray.getJSONObject(i);
            if (authObject.getString("group_name").equals(before)) {
                authObject.put("group_name", after);

                Log.d(JSONPARSER_DEBUGGING_TAG, "changed group name from " + before + " after " + after);
            }
        } //jsonObject는 새롭게 변경됨. 이걸 다시 String으로 변경해서 파일에 써야함.

        jsonData = jsonObject.toString(); //수정된 json의 String 타입 value가 저장.

        Log.d(JSONPARSER_DEBUGGING_TAG, "IOT_from changeGroupName -> update COnfig file!!!!");
        writeConfigFile(AUTH_CONFIGURATION_FILE, jsonData, context);

        return true;
    }

    boolean writeConfigFile(String fileName, String fileContents, Context context) {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter UpdateConfigFile function");
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    void addConfigFile(Auth auth, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter addConfigFile function");
        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance
        JSONArray groupsArray = jsonObject.getJSONArray("groups");


        JSONObject newJsonObject = new JSONObject();
        JSONObject authForDevices = new JSONObject();
        newJsonObject.put("group_name", auth.getGroupID() );

        authForDevices.put("bulb1", auth.isBulb1());
        authForDevices.put("bulb2", auth.isBulb2());
        authForDevices.put("strip", auth.isLedStrip());
        authForDevices.put("camera", auth.isCamera());
        authForDevices.put("speaker", auth.isSpeaker());

        newJsonObject.put("auth", authForDevices);

        groupsArray.put(newJsonObject);
        //여기까지 오면 새로운 group이 추가된 json Object를 갖게된다. 이제 이걸 다시 String으로 바꿔서 파일에 써야한다.

        String jsonString = jsonObject.toString();
        if (writeConfigFile(AUTH_CONFIGURATION_FILE, jsonString, context)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "add group Success!!!");
        }

    }

}
