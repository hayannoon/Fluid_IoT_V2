package com.example.iot_master_prototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class GoogleHomeMain extends Activity {

    final static String GOOGLE_HOME_DEGUGGING_TAG = "IoT_Google_Home_Main";
    final String[] words = new String[]{"master", "User1", "User2"};

    List<String> mSelectedItem;
    AlertDialog.Builder builder;
    JsonParser jp = new JsonParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_home_main);

        Button ledStripButton = (Button) findViewById(R.id.LED_strip_button); //LED-STRIP BUTTON
        ledStripButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG, "click LED Strip!");
                try {
                    showDialog("strip");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ledStripButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG, "LONG_CLICK LED Strip!");

                Intent intent = new Intent(getApplicationContext(), GoogleHome_LED_Strip.class);
                startActivity(intent);

                return true;
            }
        });


        Button rapoBulbButton = (Button) findViewById(R.id.rapo_smart_bulb_button);
        rapoBulbButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG, "click Rapo Bulb!");
                try {
                    showDialog("bulb1");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        Button smartLEDStandButon = (Button) findViewById(R.id.smart_led_stand_button);
        smartLEDStandButon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG, "click LED STAND!");
                try {
                    showDialog("bulb2");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        Button homeCameraButton = (Button) findViewById(R.id.home_camera_button);
        homeCameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG, "click Camera!");
                try {
                    showDialog("camera");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        Button speakerButton = (Button) findViewById(R.id.galaxy_home_mini_button);
        speakerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(GOOGLE_HOME_DEGUGGING_TAG, "click Speaker!");
                try {
                    showDialog("speaker");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void showDialog(String deviceName) throws JSONException, ExecutionException, InterruptedException {
        mSelectedItem = new ArrayList<>();
        builder = new AlertDialog.Builder(GoogleHomeMain.this);
        builder.setTitle(deviceName + " access setting");

        String[] myItem = null;
        try {
            myItem = (String[]) jp.getGroupList().toArray(new String[0]);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<Boolean> checkedItemsArray = jp.getGroupInfoAboutDevice(deviceName);
        boolean[] checkedItems = new boolean[checkedItemsArray.size()];
        for (int n = 0; n < checkedItemsArray.size(); n++) {
            checkedItems[n] = checkedItemsArray.get(n);
        }
        //click event

        String[] finalMyItem = myItem;

        for (int i = 0; i < checkedItems.length; i++) {
            if (checkedItems[i]) {
                mSelectedItem.add(finalMyItem[i]);
            }
        }

        builder.setMultiChoiceItems(myItem, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                //데이터 리스트 담기


                if (isChecked) {
                    mSelectedItem.add(finalMyItem[which]);
                } else if (mSelectedItem.contains(finalMyItem[which])) {
                    mSelectedItem.remove(finalMyItem[which]);
                }
            }
        });


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String final_section = "";

                try {
                    jp.updateConfigFileV2((ArrayList<String>) mSelectedItem, deviceName);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(), deviceName + "access control setting completed", Toast.LENGTH_LONG).show();

            }
        });

        //취소 이벤트
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "access control setting canceled", Toast.LENGTH_LONG).show();

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

}
