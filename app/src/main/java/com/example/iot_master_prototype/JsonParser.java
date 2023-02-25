package com.example.iot_master_prototype;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    final static String JSONPARSER_DEBUGGING_TAG = "IOT_JsonParser";
    final static String AUTH_CONFIGURATION_FILE = "configuration.json";
    final static String ACCOUNT_FILE = "account.json";
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
            "        \"strip\" : \"false\",\n" +
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

    final static String DEFAULT_ACCOUNT_STRING = "{\n" +
            "  \"accounts\": [\n" +
            "    {\n" +
            "      \"user_id\": \"level1_user\",\n" +
            "      \"user_pw\": \"user1_password\",\n" +
            "      \"group_name\": \"onlyBulb\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"user_id\": \"level2_user\",\n" +
            "      \"user_pw\": \"user2_password\",\n" +
            "      \"group_name\": \"onlyCamera\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"user_id\": \"master_user\",\n" +
            "      \"user_pw\": \"master_password\",\n" +
            "      \"group_name\": \"master\"\n" +
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


//    boolean changeGroupName(String before, String after, Context context) throws JSONException, FileNotFoundException {
//        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
//        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance
//
//        JSONArray authArray = jsonObject.getJSONArray("groups");
//
//        for (int i = 0; i < authArray.length(); i++) {
//            JSONObject authObject = authArray.getJSONObject(i);
//            if (authObject.getString("group_name").equals(before)) {
//                authObject.put("group_name", after);
//
//                Log.d(JSONPARSER_DEBUGGING_TAG, "changed group name from " + before + " after " + after);
//            }
//        } //jsonObject는 새롭게 변경됨. 이걸 다시 String으로 변경해서 파일에 써야함.
//
//        jsonData = jsonObject.toString(); //수정된 json의 String 타입 value가 저장.
//
//        Log.d(JSONPARSER_DEBUGGING_TAG, "IOT_from changeGroupName -> update COnfig file!!!!");
//        writeConfigFile(AUTH_CONFIGURATION_FILE, jsonData, context);
//
//        return true;
//    }

    boolean writeConfigFile(String fileName, String fileContents, Context context) {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter function to write" + fileName + "file");
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    boolean addConfigFile(Auth auth, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter addConfigFile function");
        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance
        JSONArray groupsArray = jsonObject.getJSONArray("groups");


        JSONObject newJsonObject = new JSONObject();
        JSONObject authForDevices = new JSONObject();
        newJsonObject.put("group_name", auth.getGroupID());

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
            return true;
        }
        return false;
    }

    boolean addAccountFile(Account account, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter addAccountFile function");
        String jsonData = this.getJsonString(ACCOUNT_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance
        JSONArray accountsArray = jsonObject.getJSONArray("accounts");

        JSONObject newJsonObject = new JSONObject();
        newJsonObject.put("user_id", account.getUserID());
        newJsonObject.put("user_pw", account.getUserPW());
        newJsonObject.put("group_name", account.getGroup());

        // 중복 ID 검사
        String newUserID = account.getUserID();
        for (int i = 0; i < accountsArray.length(); i++) {
            JSONObject accountObject = accountsArray.getJSONObject(i);
            if (accountObject.getString("user_id").equals(newUserID)) {
                return false;
            }
        }

        accountsArray.put(newJsonObject);
        String jsonString = jsonObject.toString();
        if (writeConfigFile(ACCOUNT_FILE, jsonString, context)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "add account success!!!");
            return true;
        }
        return true;
    }


    boolean updateConfigFile(int index, Auth auth, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter Update ConfigFile function");

        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance

        JSONArray groupArray = jsonObject.getJSONArray("groups"); //다시 json 불러와서 해당 인덱스의 auth 객체를 교체해준다.
        JSONObject targetGroup = (JSONObject) groupArray.get(index);

        JSONObject updatedDeviceAuth = new JSONObject();
        updatedDeviceAuth.put("bulb1", auth.isBulb1());
        updatedDeviceAuth.put("bulb2", auth.isBulb2());
        updatedDeviceAuth.put("strip", auth.isLedStrip());
        updatedDeviceAuth.put("camera", auth.isCamera());
        updatedDeviceAuth.put("speaker", auth.isSpeaker());


        targetGroup.put("auth", updatedDeviceAuth); //선택한 그룹의 권한 객체를 교체한다.

        String jsonString = jsonObject.toString();
        if (writeConfigFile(AUTH_CONFIGURATION_FILE, jsonString, context)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "update group Success!!!");
            return true;
        }
        return false;
    }

    boolean updateAccountFile(int index, Account account, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter Update Account function");
        String jsonData = this.getJsonString(ACCOUNT_FILE, context);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray accountArray = jsonObject.getJSONArray("accounts");
        JSONObject targetAccount = (JSONObject) accountArray.get(index);

        targetAccount.put("user_id", account.getUserID());
        targetAccount.put("user_pw", account.getUserPW());
        targetAccount.put("group_name", account.getGroup());//선택한  account의 값들을 바꿔준다.

        String jsonString = jsonObject.toString();
        if (writeConfigFile(ACCOUNT_FILE, jsonString, context)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "update account Success!!!");
            return true;
        }
        return false;
    }


    List<Auth> getAuthListFromConfigFile(Context context) throws FileNotFoundException { //read configuration file then return auth List
        List<Auth> authList = new ArrayList<>();
        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            JSONArray authArray = jsonObject.getJSONArray("groups");

            for (int i = 0; i < authArray.length(); i++) {
                JSONObject authObject = authArray.getJSONObject(i);
                Auth auth = new Auth();
                auth.setGroupID(authObject.getString("group_name"));

                JSONObject authDeviceObject = authObject.getJSONObject("auth");
                auth.setBulb1(authDeviceObject.getBoolean("bulb1"));
                auth.setBulb2(authDeviceObject.getBoolean("bulb2"));
                auth.setLedStrip(authDeviceObject.getBoolean("strip"));
                auth.setCamera(authDeviceObject.getBoolean("camera"));
                auth.setSpeaker(authDeviceObject.getBoolean("speaker"));
                //complete the auth instance

                Log.d(JSONPARSER_DEBUGGING_TAG, "Auth Instance " + i + " added!");
                authList.add(auth);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(JSONPARSER_DEBUGGING_TAG, "Length of auth list : " + authList.size());
        return authList;
    }

    List<Account> getAccountListFromAccountFile(Context context) throws FileNotFoundException, JSONException {
        List<Account> accountList = new ArrayList<>();
        String jsonData = this.getJsonString(ACCOUNT_FILE, context);

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray accountArray = jsonObject.getJSONArray("accounts");

        for (int i = 0; i < accountArray.length(); i++) {
            JSONObject accountObject = accountArray.getJSONObject(i);
            Account account = new Account();
            account.setUserID(accountObject.getString("user_id"));
            account.setUserPW(accountObject.getString("user_pw"));
            account.setGroup(accountObject.getString("group_name"));
            //complete the account instance!

            Log.d(JSONPARSER_DEBUGGING_TAG, "Account Instance " + i + " added!");
            accountList.add(account);

        }

        return accountList;
    }


    boolean removeGroup(int index, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter delte group function");

        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance

        JSONArray groupArray = jsonObject.getJSONArray("groups"); //다시 json 불러와서 해당 인덱스의 auth 객체를 교체해준다.
        groupArray.remove(index); //특정 인덱스의 항목 삭제

        String jsonString = jsonObject.toString();
        if (writeConfigFile(AUTH_CONFIGURATION_FILE, jsonString, context)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "delete group Success!!!");
            return true;
        }
        Log.d(JSONPARSER_DEBUGGING_TAG, "delete group Filed");

        return false;
    }

    boolean removeAccount(int index, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter delte account function");

        String jsonData = this.getJsonString(ACCOUNT_FILE, context);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray accountArray = jsonObject.getJSONArray("accounts");
        accountArray.remove(index);

        String jsonString = jsonObject.toString();
        if (writeConfigFile(ACCOUNT_FILE, jsonString, context)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "delete account Success!!!");
            return true;
        }
        return false;
    }

}
