package com.seg2105.doooge.choreassistant;

/**
 * Created by fredpan on 2017/11/21.
 */

class Identification {
    private final String userName;
    private final int userID;
    private final boolean isAdmin;
    private final String encrypted;

    public Identification(String userName, String encrypted, int userID, boolean isAdmin){
        this.userName = userName;
        this.userID = userID;
        this.isAdmin = isAdmin;
        this.encrypted = encrypted;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserID() {
        return userID;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getEncrypted() {
        return encrypted;
    }

}
