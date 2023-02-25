package com.example.iot_master_prototype;

public class Account {

    private String userID;
    private String userPW;
    private String group;

    public Account(String userID, String userPW, String group) {
        this.userID = userID;
        this.userPW = userPW;
        this.group = group;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserPW() {
        return userPW;
    }

    public String getGroup() {
        return group;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
