package com.example.iot_master_prototype;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class JsonParser  {

    final static String JSONPARSER_DEBUGGING_TAG = "IOT_JsonParser";
    final static String AUTH_CONFIGURATION_FILE = "configuration.json";
    final static String AUTH_CONFIGURATION_FILE_V2 = "configuration_v2.json";
    final static String ACCOUNT_FILE = "account.json";
    final static String DEVICES_INFO_FILE = "devices_info.json";
    final static String SERVER_URL = "http://13.125.172.116:8080/iot_auth/";
    private String tmp = null;

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

    final static String DEFAULT_CONFIG_V2_STRING = "{\n" +
            "  \"groups\": [\n" +
            "    {\n" +
            "      \"group_name\": \"master\",\n" +
            "      \"auth\": {\n" +
            "        \"bulb1\": {\n" +
            "          \"on/off\": \"true\",\n" +
            "          \"brightness\": \"true\",\n" +
            "          \"supervised\": \"onlyBulb\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"23 : 10\",\n" +
            "            \"end_time\": \"14 : 10\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"bulb2\": {\n" +
            "          \"on/off\": \"true\",\n" +
            "          \"brightness\": \"true\",\n" +
            "          \"supervised\": \"master\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"true\",\n" +
            "            \"start_time\": \"12 : 15\",\n" +
            "            \"end_time\": \"15 : 7\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"strip\": {\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"brightness\": \"true\",\n" +
            "          \"supervised\": \"None\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"\",\n" +
            "            \"end_time\": \"\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"camera\": \"true\",\n" +
            "        \"speaker\": {\n" +
            "          \"volume\": \"true\",\n" +
            "          \"mute\": \"true\",\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"start/stop\": \"true\",\n" +
            "          \"supervised\": \"master\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"10:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"group_name\": \"onlyBulb\",\n" +
            "      \"auth\": {\n" +
            "        \"bulb1\": {\n" +
            "          \"on/off\": \"true\",\n" +
            "          \"brightness\": \"true\",\n" +
            "          \"supervised\": \"None\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"true\",\n" +
            "            \"start_time\": \"14:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"bulb2\": {\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"brightness\": \"true\",\n" +
            "          \"supervised\": \"None\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"14:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"strip\": {\n" +
            "          \"on/off\": \"true\",\n" +
            "          \"brightness\": \"false\",\n" +
            "          \"supervised\": \"None\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"true\",\n" +
            "            \"start_time\": \"18:00\",\n" +
            "            \"end_time\": \"20:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"camera\": \"false\",\n" +
            "        \"speaker\": {\n" +
            "          \"volume\": \"true\",\n" +
            "          \"mute\": \"true\",\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"start/stop\": \"false\",\n" +
            "          \"supervised\": \"master\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"true\",\n" +
            "            \"start_time\": \"10:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"group_name\": \"onlyCamera\",\n" +
            "      \"auth\": {\n" +
            "        \"bulb1\": {\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"brightness\": \"false\",\n" +
            "          \"supervised\": \"onlyBulb\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"14:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"bulb2\": {\n" +
            "          \"on/off\": \"true\",\n" +
            "          \"brightness\": \"true\",\n" +
            "          \"supervised\": \"master\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"true\",\n" +
            "            \"start_time\": \"12 : 15\",\n" +
            "            \"end_time\": \"15 : 7\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"strip\": {\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"brightness\": \"false\",\n" +
            "          \"supervised\": \"onlyBulb\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"true\",\n" +
            "            \"start_time\": \"08:00\",\n" +
            "            \"end_time\": \"19:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"camera\": \"true\",\n" +
            "        \"speaker\": {\n" +
            "          \"volume\": \"true\",\n" +
            "          \"mute\": \"true\",\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"start/stop\": \"true\",\n" +
            "          \"supervised\": \"master\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"10:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"group_name\": \"onlySpeaker\",\n" +
            "      \"auth\": {\n" +
            "        \"bulb1\": {\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"brightness\": \"false\",\n" +
            "          \"supervised\": \"None\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"14:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"bulb2\": {\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"brightness\": \"false\",\n" +
            "          \"supervised\": \"None\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"14:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"strip\": {\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"brightness\": \"false\",\n" +
            "          \"supervised\": \"None\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"false\",\n" +
            "            \"start_time\": \"14:00\",\n" +
            "            \"end_time\": \"16:00\"\n" +
            "          }\n" +
            "        },\n" +
            "        \"camera\": \"false\",\n" +
            "        \"speaker\": {\n" +
            "          \"volume\": \"false\",\n" +
            "          \"mute\": \"true\",\n" +
            "          \"on/off\": \"false\",\n" +
            "          \"start/stop\": \"true\",\n" +
            "          \"supervised\": \"onlyBulb\",\n" +
            "          \"temporal\": {\n" +
            "            \"isTemporal\": \"true\",\n" +
            "            \"start_time\": \"12:00\",\n" +
            "            \"end_time\": \"18:00\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    final static String DEFAULT_ACCOUNT_STRING = "{\n" +
            "  \"accounts\": [\n" +
            "    {\n" +
            "      \"user_id\": \"level1\",\n" +
            "      \"user_pw\": \"l1\",\n" +
            "      \"group_name\": \"onlyBulb\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"user_id\": \"level2\",\n" +
            "      \"user_pw\": \"l2\",\n" +
            "      \"group_name\": \"onlyCamera\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"user_id\": \"master\",\n" +
            "      \"user_pw\": \"m\",\n" +
            "      \"group_name\": \"master\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"user_id\": \"a\",\n" +
            "      \"user_pw\": \"b\",\n" +
            "      \"group_name\": \"master\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";



    final static String DEFAULT_DEVICES_INFO_STRING = "{\n" +
            "  \"devices\": [\n" +
            "    {\n" +
            "      \"device_name\": \"bulb1\",\n" +
            "      \"id\": \"control_base_item\",\n" +
            "      \"title\": \"rapo smart bulb\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"device_name\": \"bulb2\",\n" +
            "      \"id\": \"control_base_item\",\n" +
            "      \"title\": \"Smart LED Stand\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"device_name\": \"camera\",\n" +
            "      \"id\": \"control_base_item\",\n" +
            "      \"title\": \"Home camera 360\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"device_name\": \"speaker\",\n" +
            "      \"id\": \"control_base_item\",\n" +
            "      \"title\": \"Galaxy Home Mini\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    Context context;

    public JsonParser(Context context) {
        this.context = context;
    }

    public JsonParser() {

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

    String getJsonStringFromServer(String fileName) throws ExecutionException, InterruptedException {
        NetworkConnectionForReadJSON net = new NetworkConnectionForReadJSON(fileName);
        return net.execute().get();
    }


    boolean writeConfigFile(String fileName, String fileContents, Context context) {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter function to write" + fileName + "file");
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    boolean writeConfigFileToServer(String fileName, String fileContents) throws InterruptedException {
        //file Name과 contetnt를 받아와서 서버에 있는 fileName파일에 fileContetns를 Write한다.
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter writeConfigFileToServer FUnction");

        NetworkConnectForWriteJSON net = new NetworkConnectForWriteJSON(fileName, fileContents);
        net.start();
        net.join();
        return net.getResult();
    }




    boolean addConfigFile(Auth auth, Context context) throws FileNotFoundException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter addConfigFile function");
        String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance
        JSONArray groupsArray = jsonObject.getJSONArray("groups");


        JSONObject newJsonObject = new JSONObject();
        JSONObject authForDevices = new JSONObject();
        newJsonObject.put("group_name", auth.getGroupID());

        authForDevices.put("bulb1", String.valueOf(auth.isBulb1()));
        authForDevices.put("bulb2", String.valueOf(auth.isBulb2()));
        authForDevices.put("strip", String.valueOf(auth.isLedStrip()));
        authForDevices.put("camera", String.valueOf(auth.isCamera()));
        authForDevices.put("speaker", String.valueOf(auth.isSpeaker()));

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

    boolean addConfigFileToServer(Auth auth) throws JSONException, ExecutionException, InterruptedException {
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE); //get Json Data from server and save the string value

        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance
        JSONArray groupsArray = jsonObject.getJSONArray("groups");


        JSONObject newJsonObject = new JSONObject();
        JSONObject authForDevices = new JSONObject();
        newJsonObject.put("group_name", auth.getGroupID());


        authForDevices.put("bulb1", String.valueOf(auth.isBulb1()));
        authForDevices.put("bulb2", String.valueOf(auth.isBulb2()));
        authForDevices.put("strip", String.valueOf(auth.isLedStrip()));
        authForDevices.put("camera", String.valueOf(auth.isCamera()));
        authForDevices.put("speaker", String.valueOf(auth.isSpeaker()));

        newJsonObject.put("auth", authForDevices);

        groupsArray.put(newJsonObject);
        //여기까지 오면 새로운 group이 추가된 json Object를 갖게된다. 이제 이걸 다시 String으로 바꿔서 파일에 써야한다.

        String jsonString = jsonObject.toString();
        if (this.writeConfigFileToServer(AUTH_CONFIGURATION_FILE, jsonString)) {
            return true;
        } else {
            return false;
        }
    }

    String  getUIInfoFromAccount(String userID, String userPW) throws ExecutionException, InterruptedException, JSONException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter getUIInfoFromAccount function");

        String jsonData = this.getJsonStringFromServer(ACCOUNT_FILE);
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance
        JSONArray accountsArray = jsonObject.getJSONArray("accounts");
        JSONArray groupArray;
        JSONArray devicesArray;
        String returnValue = "{\"devices\":[";

        String selectedGroupName = null;
        for (int i = 0; i < accountsArray.length(); i++) {
            JSONObject accountObject = accountsArray.getJSONObject(i);
            if (accountObject.getString("user_id").equals(userID)) {
                if(accountObject.getString("user_pw").equals(userPW)){
                    //ID - PW MATCHING
                    selectedGroupName = new String(accountObject.getString("group_name")); //Group Name Initialize
                } else{
                    // return 1 = ID - PW NOT MATCHING
                    return "1";
                }
            }
        }

        if(selectedGroupName == null){
            //return 2 = NO MATCHING ID
            return "2";
        }


        //여기까지 왔다면 ID/PW는 매칭되는게 있고, Group Name까지 저장이 된 상태
        //이제 Group파일을 보고 True로 설정된 기기의 리스트를 가져온다.

        jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE);
        jsonObject = new JSONObject(jsonData);
        groupArray = jsonObject.getJSONArray("groups");

        JSONObject targetGroup = null;
        for(int i = 0 ; i < groupArray.length(); i++){
            JSONObject groupObject = groupArray.getJSONObject(i);
            if(groupObject.getString("group_name").equals(selectedGroupName)){
                targetGroup = groupObject;
                break;
            }
        }
        if(targetGroup == null){
            //return 3 = NO MATCHING GROUP
            return "3";
        }

        //여기까지 왔다면 Group 정보를 가져와서 targetGroup객체에 저장이 완료된 상태.
        ArrayList<String> allowdDevicesList = new ArrayList<>();
        JSONObject Auths = targetGroup.getJSONObject("auth");

        if(Auths.getString("bulb1").toString().equals("true")) allowdDevicesList.add("bulb1");
        if(Auths.getString("bulb2").toString().equals("true")) allowdDevicesList.add("bulb2");
        if(Auths.getString("strip").toString().equals("true")) allowdDevicesList.add("strip");
        if(Auths.getString("camera").toString().equals("true")) allowdDevicesList.add("camera");
        if(Auths.getString("speaker").toString().equals("true")) allowdDevicesList.add("speaker");

        Log.d(JSONPARSER_DEBUGGING_TAG, "NUMBER OF ALLOWED DEVICES : " + allowdDevicesList.size());
        //여기까지 왔다면  alloweddDevicesList 허용된 디바이스 리스트가 저장된 상태

        jsonData = this.getJsonStringFromServer(DEVICES_INFO_FILE);
        jsonObject = new JSONObject(jsonData);
        devicesArray = jsonObject.getJSONArray("devices");

        for(int i = 0 ; i < allowdDevicesList.size() ; i++){
            String deviceName = allowdDevicesList.get(i).toString();
            for(int j = 0 ; j < devicesArray.length() ; j++){
                JSONObject deviceObject = devicesArray.getJSONObject(j);
                if(deviceObject.getString("device_name").equals(deviceName)){
                    String tmp = deviceObject.toString();
                    returnValue += tmp;
                    returnValue += ",";
                }
            }
        }

        returnValue = returnValue.substring(0, returnValue.length() - 1);
        returnValue += "]}";

        Log.d(JSONPARSER_DEBUGGING_TAG, returnValue);

        return returnValue;
    }


    boolean addAccountFile(Account account) throws FileNotFoundException, JSONException, ExecutionException, InterruptedException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter addAccountFile function");

        String jsonData = this.getJsonStringFromServer(ACCOUNT_FILE);
        //String jsonData = this.getJsonString(ACCOUNT_FILE, context); //saved json String data
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

        if (writeConfigFileToServer(ACCOUNT_FILE, jsonString)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "add account success!!!");
            return true;
        }

        return false;
    }


    boolean updateConfigFile(int index, Auth auth, Context context) throws FileNotFoundException, JSONException, ExecutionException, InterruptedException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter Update ConfigFile function");
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE); //get Json Data from server and save the string value

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

        if (writeConfigFileToServer(AUTH_CONFIGURATION_FILE, jsonString)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "update group Success!!!");
            return true;
        }

        return false;
    }

    boolean updateConfigFile_V2_Bulb1(int index, Auth auth) throws ExecutionException, InterruptedException, JSONException {
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE_V2);

        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray groupArray = jsonObject.getJSONArray("groups"); //다시 json 불러와서 해당 인덱스의 auth 객체를 교체해준다.
        JSONObject targetGroup = (JSONObject) groupArray.get(index);

        JSONObject authOfTargetGroup = targetGroup.getJSONObject("auth");

        //JSONObject updateDeviceAuth = new JSONObject();
        //여기서 정보 update

        //Update Bulb 1
        JSONObject newBulb1 = new JSONObject();
        JSONObject newBulb1Temporal = new JSONObject();
        newBulb1.put("on/off", Boolean.toString(auth.isBulb1OnOff()));
        newBulb1.put("brightness", Boolean.toString(auth.isBulb1Brightness()));
        newBulb1.put("supervised", auth.getBulb1SupervisedBy());
        newBulb1Temporal.put("isTemporal", Boolean.toString(auth.isBulb1IsTemporal()));
        newBulb1Temporal.put("start_time", auth.getBulb1StartTime());
        newBulb1Temporal.put("end_time", auth.getBulb1EndTime());
        newBulb1.put("temporal", newBulb1Temporal);

        authOfTargetGroup.put("bulb1", newBulb1);

        String jsonString = jsonObject.toString();

        if(writeConfigFileToServer(AUTH_CONFIGURATION_FILE_V2, jsonString)){
            return true;
        }

        return false;
    }

    boolean updateConfigFile_V2_Bulb2(int index, Auth auth) throws ExecutionException, InterruptedException, JSONException {
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE_V2);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray groupArray = jsonObject.getJSONArray("groups");
        JSONObject targetGroup = (JSONObject) groupArray.get(index);

        JSONObject authOfTargetGroup = targetGroup.getJSONObject("auth");

        JSONObject newBulb2 = new JSONObject();
        JSONObject newBulb2Temporal = new JSONObject();
        newBulb2.put("on/off", Boolean.toString(auth.isBulb2OnOff()));
        newBulb2.put("brightness", Boolean.toString(auth.isBulb2Brightness()));
        newBulb2.put("supervised", auth.getBulb2SupervisedBy());
        newBulb2Temporal.put("isTemporal", Boolean.toString(auth.isBulb2IsTemporal()));
        newBulb2Temporal.put("start_time", auth.getBulb2StartTime());
        newBulb2Temporal.put("end_time", auth.getBulb2EndTime());
        newBulb2.put("temporal", newBulb2Temporal);

        authOfTargetGroup.put("bulb2", newBulb2);

        String jsonString = jsonObject.toString();

        if(writeConfigFileToServer(AUTH_CONFIGURATION_FILE_V2, jsonString)){
            return true;
        }

        return false;
    }

    boolean updateConfigFile_V2_LED_STRIP(int index, Auth auth) throws ExecutionException, InterruptedException, JSONException {
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE_V2);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray groupArray = jsonObject.getJSONArray("groups");
        JSONObject targetGroup = (JSONObject) groupArray.get(index);

        JSONObject authOfTargetGroup = targetGroup.getJSONObject("auth");

        JSONObject newStrip = new JSONObject();
        JSONObject newStripTemporal = new JSONObject();
        newStrip.put("on/off", Boolean.toString(auth.isLedStripOnOff()));
        newStrip.put("brightness", Boolean.toString(auth.isLedStripBrightness()));
        newStrip.put("supervised", auth.getLedStripSupervisedBy());
        newStripTemporal.put("isTemporal", Boolean.toString(auth.isLedStripIsTemporal()));
        newStripTemporal.put("start_time", auth.getLedStripStartTime());
        newStripTemporal.put("end_time", auth.getLedStripEndTime());
        newStrip.put("temporal", newStripTemporal);

        authOfTargetGroup.put("strip", newStrip);

        String jsonString = jsonObject.toString();

        if(writeConfigFileToServer(AUTH_CONFIGURATION_FILE_V2, jsonString)){
            return true;
        }
        return false;
    }

    boolean updateConfigFile_V2_SPEAKER(int index, Auth auth) throws ExecutionException, InterruptedException, JSONException {
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE_V2);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray groupArray = jsonObject.getJSONArray("groups");
        JSONObject targetGroup = (JSONObject) groupArray.get(index);

        JSONObject authOfTargetGroup = targetGroup.getJSONObject("auth");

        JSONObject newSpeaker = new JSONObject();
        JSONObject newSpeakerTemporal = new JSONObject();
        newSpeaker.put("volume", Boolean.toString(auth.isSpeakerVolume()));
        newSpeaker.put("mute", Boolean.toString(auth.isSpeakerMute()));
        newSpeaker.put("on/off", Boolean.toString(auth.isSpeakerOnOff()));
        newSpeaker.put("start/stop", Boolean.toString(auth.isSpeakerStartStop()));
        newSpeaker.put("supervised", auth.getSpeakerSupervisedBy());
        newSpeakerTemporal.put("isTemporal", Boolean.toString(auth.isSpeakerIsTemporal()));
        newSpeakerTemporal.put("start_time", auth.getSpeakerStartTime());
        newSpeakerTemporal.put("end_time", auth.getSpeakerEndTime());
        newSpeaker.put("temporal",newSpeakerTemporal);

        authOfTargetGroup.put("speaker", newSpeaker);

        String jsonString = jsonObject.toString();

        if(writeConfigFileToServer(AUTH_CONFIGURATION_FILE_V2, jsonString)){
            return true;
        }
        return false;
    }



    boolean updateConfigFileV2(ArrayList<String> allowdGroupArray, String deviceName) throws ExecutionException, InterruptedException, JSONException {
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE); //get Json Data from server and save the string value

        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance

        JSONArray groupArray = jsonObject.getJSONArray("groups"); //다시 json 불러와서 해당 인덱스의 auth 객체를 교체해준다.

        for(int i = 0 ; i < groupArray.length() ; i++){
            JSONObject targetObject = (JSONObject) groupArray.get(i);
            String groupName = targetObject.getString("group_name");
            JSONObject tmp = targetObject.getJSONObject("auth");
            if(allowdGroupArray.contains(groupName)){
                tmp.put(deviceName, "true");
            } else{
                tmp.put(deviceName, "false");
            }
        }

        String jsonString = jsonObject.toString();
        if (writeConfigFileToServer(AUTH_CONFIGURATION_FILE, jsonString)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "update group Success!!!");
            return true;
        }
        return false;
    }



    ArrayList<String> getGroupList() throws ExecutionException, InterruptedException, JSONException {
        ArrayList<String> groupList = new ArrayList<>();
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE);
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject tmp;

        JSONArray groupArray = jsonObject.getJSONArray("groups");
        for(int i = 0 ; i < groupArray.length() ; i++){
            tmp = (JSONObject) groupArray.get(i);
            groupList.add(tmp.getString("group_name"));
        }

        return groupList;
    }

    ArrayList<Boolean> getGroupInfoAboutDevice(String deviceName) throws ExecutionException, JSONException, InterruptedException {
        ArrayList<Boolean> groupInfoAboutDeviceArray = new ArrayList<>();
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE);
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject tmp;

        JSONArray groupArray = jsonObject.getJSONArray("groups");
        for(int i = 0 ; i < groupArray.length() ; i++){
            tmp = (JSONObject) groupArray.get(i);
            JSONObject tmp1 = tmp.getJSONObject("auth");
            groupInfoAboutDeviceArray.add(Boolean.parseBoolean(tmp1.getString(deviceName)));
        }

        return groupInfoAboutDeviceArray;
    }




    boolean removeGroup(int index, Context context) throws FileNotFoundException, JSONException, InterruptedException, ExecutionException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter delte group function");

        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE);
        //String jsonData = this.getJsonString(AUTH_CONFIGURATION_FILE, context); //saved json String data
        JSONObject jsonObject = new JSONObject(jsonData); //make jsonObject instance

        JSONArray groupArray = jsonObject.getJSONArray("groups"); //다시 json 불러와서 해당 인덱스의 auth 객체를 교체해준다.
        groupArray.remove(index); //특정 인덱스의 항목 삭제

        String jsonString = jsonObject.toString();

        if (writeConfigFileToServer(AUTH_CONFIGURATION_FILE, jsonString)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "delete group Success!!!");
            return true;
        }


        Log.d(JSONPARSER_DEBUGGING_TAG, "delete group Filed");

        return false;
    }

    boolean updateAccountFile(int index, Account account, Context context) throws FileNotFoundException, JSONException, ExecutionException, InterruptedException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter Update Account function");

        String jsonData = this.getJsonStringFromServer(ACCOUNT_FILE);
        //String jsonData = this.getJsonString(ACCOUNT_FILE, context);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray accountArray = jsonObject.getJSONArray("accounts");
        JSONObject targetAccount = (JSONObject) accountArray.get(index);

        targetAccount.put("user_id", account.getUserID());
        targetAccount.put("user_pw", account.getUserPW());
        targetAccount.put("group_name", account.getGroup());//선택한  account의 값들을 바꿔준다.

        String jsonString = jsonObject.toString();
        if (writeConfigFileToServer(ACCOUNT_FILE, jsonString)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "update account Success!!!");
            return true;
        }

        return false;
    }

    List<Auth> getAuthListFromCOnfigFile_v2() throws ExecutionException, InterruptedException, JSONException {
        List<Auth> authList = new ArrayList<>();
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE_V2);

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray authArray = jsonObject.getJSONArray("groups");

        for(int i = 0 ; i < authArray.length() ; i++){
            JSONObject authObject = authArray.getJSONObject(i);
            Auth auth = new Auth();
            auth.setGroupID(authObject.getString("group_name")); //get Name

            JSONObject authDeviceObject = authObject.getJSONObject("auth");
            JSONObject bulb1Object = authDeviceObject.getJSONObject("bulb1");
            JSONObject bulb2Object = authDeviceObject.getJSONObject("bulb2");
            JSONObject ledStripObject = authDeviceObject.getJSONObject("strip");
            JSONObject speakerObject = authDeviceObject.getJSONObject("speaker");

            auth.setBulb1OnOff(bulb1Object.getBoolean("on/off"));
            auth.setBulb1Brightness(bulb1Object.getBoolean("brightness"));
            String isSupervised = bulb1Object.getString("supervised");
            if(isSupervised.equals("None")){
                auth.setBulb1IsSupervised(false);
            }else{
                auth.setBulb1IsSupervised(true);
                auth.setBulb1SupervisedBy(isSupervised);
            }

            JSONObject isTemporalObject = bulb1Object.getJSONObject("temporal");
            auth.setBulb1IsTemporal(isTemporalObject.getBoolean("isTemporal"));
            auth.setBulb1StartTime(isTemporalObject.getString("start_time"));
            auth.setBulb1EndTime(isTemporalObject.getString("end_time"));
            //여기까지가 Bulb1

            auth.setBulb2OnOff(bulb2Object.getBoolean("on/off"));
            auth.setBulb2Brightness(bulb2Object.getBoolean("brightness"));
            isSupervised = bulb2Object.getString("supervised");
            if(isSupervised.equals("None")){
                auth.setBulb2IsSupervised(false);
            }else{
                auth.setBulb2IsSupervised(true);
                auth.setBulb2SupervisedBy(isSupervised);
            }

            isTemporalObject = bulb2Object.getJSONObject("temporal");
            auth.setBulb2IsTemporal(isTemporalObject.getBoolean("isTemporal"));
            auth.setBulb2StartTime(isTemporalObject.getString("start_time"));
            auth.setBulb2EndTime(isTemporalObject.getString("end_time"));
            //여기까지가 bulb2

            auth.setLedStripOnOff(ledStripObject.getBoolean("on/off"));
            auth.setLedStripBrightness(ledStripObject.getBoolean("brightness"));
            isSupervised = ledStripObject.getString("supervised");
            if(isSupervised.equals("None")){
                auth.setLedStripIsSupervised(false);
            }else{
                auth.setLedStripIsSupervised(true);
                auth.setLedStripSupervisedBy(isSupervised);
            }

            isTemporalObject = ledStripObject.getJSONObject("temporal");
            auth.setLedStripIsTemporal(isTemporalObject.getBoolean("isTemporal"));
            auth.setLedStripStartTime(isTemporalObject.getString("start_time"));
            auth.setLedStripEndTime(isTemporalObject.getString("end_time"));
            //여기까지가 LED STRIP


            auth.setSpeakerOnOff(speakerObject.getBoolean("on/off"));
            auth.setSpeakerMute(speakerObject.getBoolean("mute"));
            auth.setSpeakerVolume(speakerObject.getBoolean("volume"));
            auth.setSpeakerStartStop(speakerObject.getBoolean("start/stop"));
            isSupervised = speakerObject.getString("supervised");
            if(isSupervised.equals("None")){
                auth.setSpeakerSupervised(false);
            } else{
                auth.setSpeakerSupervised(true);
                auth.setSpeakerSupervisedBy(isSupervised);
            }

            isTemporalObject = speakerObject.getJSONObject("temporal");
            auth.setSpeakerIsTemporal(isTemporalObject.getBoolean("isTemporal"));
            auth.setSpeakerStartTime(isTemporalObject.getString("start_time"));
            auth.setSpeakerEndTime(isTemporalObject.getString("end_time"));

            auth.setCamera(authDeviceObject.getBoolean("camera"));

            Log.d(JSONPARSER_DEBUGGING_TAG, "Auth Instance " + i + " added!");
            authList.add(auth);

        }
        Log.d(JSONPARSER_DEBUGGING_TAG, "Length of auth list : " + authList.size());
        return authList;
    }



    List<Auth> getAuthListFromConfigFile(Context context) throws FileNotFoundException, ExecutionException, InterruptedException { //read configuration file then return auth List
        List<Auth> authList = new ArrayList<>();
        String jsonData = this.getJsonStringFromServer(AUTH_CONFIGURATION_FILE); //get Json Data from server and save the string value

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

    List<Account> getAccountListFromAccountFile(Context context) throws FileNotFoundException, JSONException, ExecutionException, InterruptedException {
        List<Account> accountList = new ArrayList<>();
        String jsonData = this.getJsonStringFromServer(ACCOUNT_FILE);

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


    boolean removeAccount(int index, Context context) throws FileNotFoundException, JSONException, ExecutionException, InterruptedException {
        Log.d(JSONPARSER_DEBUGGING_TAG, "Enter delte account function");

        String jsonData = this.getJsonStringFromServer(ACCOUNT_FILE);
        //String jsonData = this.getJsonString(ACCOUNT_FILE, context);
        JSONObject jsonObject = new JSONObject(jsonData);

        JSONArray accountArray = jsonObject.getJSONArray("accounts");
        accountArray.remove(index);

        String jsonString = jsonObject.toString();

        if (writeConfigFileToServer(ACCOUNT_FILE, jsonString)) {
            Log.d(JSONPARSER_DEBUGGING_TAG, "delete account Success!!!");
            return true;
        }
        return false;
    }


    class NetworkConnectForWriteJSON extends Thread { //서버에 파일을 Write할때 사용한다.

        final static String NETWORK_CONNECT_DEBUGGING_TAG = "NETWORK_CONNECT_DEBUGGING";
        private String fileContents;
        private String serverURL = "http://13.125.172.116:8080/iot_auth/";
        boolean result = false;


        public NetworkConnectForWriteJSON(String fileName, String jsonContents) {
            this.fileContents = jsonContents;
            this.serverURL = this.serverURL.concat(fileName);
        }

        @Override
        public void run() {
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
                Log.d(NETWORK_CONNECT_DEBUGGING_TAG, conn.getContent().toString());
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                    Log.d(NETWORK_CONNECT_DEBUGGING_TAG, "UPLOAD_SERVER_SUCCESS");
                    this.result = true;
                    // File uploaded successfully
                } else {
                    // File upload failed
                    Log.d(NETWORK_CONNECT_DEBUGGING_TAG, "UPLOAD_SERVER_FAILED " + responseCode);
                    this.result = false;
                }
            } catch (Exception e) {
                // Handle exception
                Log.e("[HTTP_ERROR]", e.getMessage(), e);
            }
        }

        public boolean getResult() {
            return this.result;
        }

    }

    class NetworkConnectionForReadJSON extends AsyncTask<Void, Void, String> {

        private String serverURL = JsonParser.SERVER_URL;
        private ContentValues values;

        public NetworkConnectionForReadJSON(String fileName) {
            this.serverURL = this.serverURL.concat(fileName);
            this.values = null;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(serverURL, values); // 해당 URL로 부터 결과물을 얻어온다.

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            //내용이 s에 저장된다. 이 s를 json파일에 저장해주면 될듯하다.
        }
    }


}
