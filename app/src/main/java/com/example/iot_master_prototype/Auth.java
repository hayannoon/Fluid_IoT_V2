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

    private boolean bulb1OnOff;
    private boolean bulb1Brightness;
    private boolean bulb1IsSupervised;
    private String bulb1SupervisedBy;
    private boolean bulb1IsTemporal;
    private String bulb1StartTime;
    private String bulb1EndTime;

    private boolean bulb2OnOff;
    private boolean bulb2Brightness;
    private boolean bulb2IsSupervised;
    private String bulb2SupervisedBy;
    private boolean bulb2IsTemporal;
    private String bulb2StartTime;
    private String bulb2EndTime;

    private boolean ledStripOnOff;
    private boolean ledStripBrightness;
    private boolean ledStripIsSupervised;
    private String ledStripSupervisedBy;
    private boolean ledStripIsTemporal;
    private String ledStripStartTime;
    private String ledStripEndTime;

    private boolean speakerVolume;
    private boolean speakerMute;
    private boolean spakerOnOff;
    private boolean speakerStartStop;
    private boolean speakerSupervised;
    private String speakerSupervisedBy;
    private boolean speakerIsTemporal;
    private String speakerStartTime;
    private String speakerEndTime;

    public boolean isSpeakerVolume() {
        return speakerVolume;
    }

    public void setSpeakerVolume(boolean speakerVolume) {
        this.speakerVolume = speakerVolume;
    }

    public boolean isSpeakerMute() {
        return speakerMute;
    }

    public void setSpeakerMute(boolean speakerMute) {
        this.speakerMute = speakerMute;
    }

    public boolean isSpakerOnOff() {
        return spakerOnOff;
    }

    public void setSpakerOnOff(boolean spakerOnOff) {
        this.spakerOnOff = spakerOnOff;
    }

    public boolean isSpeakerStartStop() {
        return speakerStartStop;
    }

    public void setSpeakerStartStop(boolean speakerStartStop) {
        this.speakerStartStop = speakerStartStop;
    }

    public boolean isSpeakerSupervised() {
        return speakerSupervised;
    }

    public void setSpeakerSupervised(boolean speakerSupervised) {
        this.speakerSupervised = speakerSupervised;
    }

    public String getSpeakerSupervisedBy() {
        return speakerSupervisedBy;
    }

    public void setSpeakerSupervisedBy(String speakerSupervisedBy) {
        this.speakerSupervisedBy = speakerSupervisedBy;
    }

    public boolean isSpeakerIsTemporal() {
        return speakerIsTemporal;
    }

    public void setSpeakerIsTemporal(boolean speakerIsTemporal) {
        this.speakerIsTemporal = speakerIsTemporal;
    }

    public String getSpeakerStartTime() {
        return speakerStartTime;
    }

    public void setSpeakerStartTime(String spakerStartTime) {
        this.speakerStartTime = spakerStartTime;
    }

    public String getSpeakerEndTime() {
        return speakerEndTime;
    }

    public void setSpeakerEndTime(String speakerEndTime) {
        this.speakerEndTime = speakerEndTime;
    }

    public boolean isBulb1OnOff() {
        return bulb1OnOff;
    }

    public void setBulb1OnOff(boolean bulb1OnOff) {
        this.bulb1OnOff = bulb1OnOff;
    }

    public boolean isBulb1Brightness() {
        return bulb1Brightness;
    }

    public void setBulb1Brightness(boolean bulb1Brightness) {
        this.bulb1Brightness = bulb1Brightness;
    }

    public boolean isBulb1IsSupervised() {
        return bulb1IsSupervised;
    }

    public void setBulb1IsSupervised(boolean bulb1IsSupervised) {
        this.bulb1IsSupervised = bulb1IsSupervised;
    }

    public String getBulb1SupervisedBy() {
        return bulb1SupervisedBy;
    }

    public void setBulb1SupervisedBy(String bulb1SupervisedBy) {
        this.bulb1SupervisedBy = bulb1SupervisedBy;
    }

    public boolean isBulb1IsTemporal() {
        return bulb1IsTemporal;
    }

    public void setBulb1IsTemporal(boolean bulb1IsTemporal) {
        this.bulb1IsTemporal = bulb1IsTemporal;
    }

    public String getBulb1StartTime() {
        return bulb1StartTime;
    }

    public void setBulb1StartTime(String bulb1StartTime) {
        this.bulb1StartTime = bulb1StartTime;
    }

    public String getBulb1EndTime() {
        return bulb1EndTime;
    }

    public void setBulb1EndTime(String bulb1EndTime) {
        this.bulb1EndTime = bulb1EndTime;
    }

    public boolean isBulb2OnOff() {
        return bulb2OnOff;
    }

    public void setBulb2OnOff(boolean bulb2OnOff) {
        this.bulb2OnOff = bulb2OnOff;
    }

    public boolean isBulb2Brightness() {
        return bulb2Brightness;
    }

    public void setBulb2Brightness(boolean bulb2Brightness) {
        this.bulb2Brightness = bulb2Brightness;
    }

    public boolean isBulb2IsSupervised() {
        return bulb2IsSupervised;
    }

    public void setBulb2IsSupervised(boolean bulb2IsSupervised) {
        this.bulb2IsSupervised = bulb2IsSupervised;
    }

    public String getBulb2SupervisedBy() {
        return bulb2SupervisedBy;
    }

    public void setBulb2SupervisedBy(String bulb2SupervisedBy) {
        this.bulb2SupervisedBy = bulb2SupervisedBy;
    }

    public boolean isBulb2IsTemporal() {
        return bulb2IsTemporal;
    }

    public void setBulb2IsTemporal(boolean bulb2IsTemporal) {
        this.bulb2IsTemporal = bulb2IsTemporal;
    }

    public String getBulb2StartTime() {
        return bulb2StartTime;
    }

    public void setBulb2StartTime(String bulb2StartTime) {
        this.bulb2StartTime = bulb2StartTime;
    }

    public String getBulb2EndTime() {
        return bulb2EndTime;
    }

    public void setBulb2EndTime(String bulb2EndTime) {
        this.bulb2EndTime = bulb2EndTime;
    }

    public boolean isLedStripOnOff() {
        return ledStripOnOff;
    }

    public void setLedStripOnOff(boolean ledStripOnOff) {
        this.ledStripOnOff = ledStripOnOff;
    }

    public boolean isLedStripBrightness() {
        return ledStripBrightness;
    }

    public void setLedStripBrightness(boolean ledStripBrightness) {
        this.ledStripBrightness = ledStripBrightness;
    }

    public boolean isLedStripIsSupervised() {
        return ledStripIsSupervised;
    }

    public void setLedStripIsSupervised(boolean ledStripIsSupervised) {
        this.ledStripIsSupervised = ledStripIsSupervised;
    }

    public String getLedStripSupervisedBy() {
        return ledStripSupervisedBy;
    }

    public void setLedStripSupervisedBy(String ledStripSupervisedBy) {
        this.ledStripSupervisedBy = ledStripSupervisedBy;
    }

    public boolean isLedStripIsTemporal() {
        return ledStripIsTemporal;
    }

    public void setLedStripIsTemporal(boolean ledStripIsTemporal) {
        this.ledStripIsTemporal = ledStripIsTemporal;
    }

    public String getLedStripStartTime() {
        return ledStripStartTime;
    }

    public void setLedStripStartTime(String ledStripStartTime) {
        this.ledStripStartTime = ledStripStartTime;
    }

    public String getLedStripEndTime() {
        return ledStripEndTime;
    }

    public void setLedStripEndTime(String ledStripEndTime) {
        this.ledStripEndTime = ledStripEndTime;
    }

    public Auth(){

    }

    public Auth(boolean b1, boolean b2, boolean led, boolean cam, boolean spk){
        this.bulb1 = b1;
        this.bulb1OnOff = b1;
        this.bulb1Brightness = b1;
        this.bulb1IsSupervised = false;
        this.bulb1IsTemporal = false;

        this.bulb2 = b2;
        this.bulb2OnOff = b2;
        this.bulb2Brightness = b2;
        this.bulb2IsSupervised = false;
        this.bulb2IsTemporal = false;

        this.ledStrip = led;
        this.ledStripOnOff = led;
        this.ledStripBrightness = led;
        this.ledStripIsSupervised = false;
        this.ledStripIsTemporal = false;

        this.camera = cam;

        this.speaker = spk;
        this.spakerOnOff = spk;
        this.speakerMute = spk;
        this.speakerVolume = spk;
        this.speakerStartStop = spk;
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
