package com.example.iot_master_prototype;

import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

public class Auth {

    private String groupID;

    private boolean bulb1;
    private boolean bulb2;
    private boolean ledStrip;
    private boolean camera;
    private boolean speaker;


    public Auth(){

    }

    public Auth(boolean b1, boolean b2, boolean led, boolean cam, boolean spk){
        this.bulb1 = b1;
        this.bulb2 = b2;
        this.ledStrip = led;
        this.camera = cam;
        this.speaker = spk;
    }

    public Auth(String groupID) {
        this.groupID = groupID;
    }


    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public boolean isBulb1() {
        return bulb1;
    }


    public void setBulb1(boolean bulb1) {
        this.bulb1 = bulb1;
    }



    public boolean isBulb2() {
        return bulb2;
    }

    public void setBulb2(boolean bulb2) {
        this.bulb2 = bulb2;
    }



    public boolean isLedStrip() {
        return ledStrip;
    }

    public void setLedStrip(boolean ledStrip) {
        this.ledStrip = ledStrip;
    }



    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }


    public boolean isSpeaker() {
        return speaker;
    }

    public void setSpeaker(boolean speaker) {
        this.speaker = speaker;
    }


}
