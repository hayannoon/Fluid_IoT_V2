package com.example.iot_master_prototype;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

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

public class JsonParser extends AppCompatActivity {

    final static String JSONPARSER_DEBUGGING_TAG = "IOT_JsonParser";
    final static String AUTH_CONFIGURATION_FILE = "configuration.json";
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

    final static String DEFAULT_DEVICES_INFO_STRING = "{\n" +
            "  \"devices\": [\n" +
            "    {\n" +
            "      \"bulb1\": {\n" +
            "        \"id\": \"control_base_item\",\n" +
            "        \"title\": \"rapo smart bulb\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"bulb2\": {\n" +
            "        \"id\": \"control_base_item\",\n" +
            "        \"title\": \"Smart LED Stand\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"camera\": {\n" +
            "        \"id\": \"control_base_item\",\n" +
            "        \"title\": \"Home camera 360\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"speaker\": {\n" +
            "        \"id\": \"control_base_item\",\n" +
            "        \"title\": \"Galaxy Home Mini\"\n" +
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


    boolean addAccountFile(Account account, Context context) throws FileNotFoundException, JSONException, ExecutionException, InterruptedException {
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
