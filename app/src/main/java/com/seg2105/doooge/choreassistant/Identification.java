package com.seg2105.doooge.choreassistant;

/**
 * Created by fredpan on 2017/11/21.
 */

public class Identification {
    private final String userName;
    private final long userID;
    private final boolean isAdmin;
    private final String encrypted;
    private final String color;

    public Identification(String userName, String encrypted, long userID, boolean isAdmin, String color) {
        this.userName = userName;
        this.userID = userID;
        this.isAdmin = isAdmin;
        this.encrypted = encrypted;
        this.color = color;
    }

    public String getUserName() {
        return userName;
    }

    public long getUserID() {
        return userID;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public String getColor() {
        return color;
    }

}
