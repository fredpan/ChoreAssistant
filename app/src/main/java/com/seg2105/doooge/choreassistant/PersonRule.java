package com.seg2105.doooge.choreassistant;

import java.io.Serializable;

/**
 * Created by fredpan on 2017/11/21.
 */

public class PersonRule implements Serializable {
    private String userName;
    private boolean admin;
    private String encrypted;
    private int color;
    private int userID;


    public PersonRule() {
        //Keep for Firebase only
    }

    public PersonRule(String userName, String encrypted, boolean admin, int color, int userID) {
        this.userName = userName;


        this.userID = userID;
        this.admin = admin;
        this.encrypted = encrypted;
        this.color = color;

    }

    public String getUserName() {
        return userName;
    }

    public int getUserID() {
        return userID;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public int getColor() {
        return color;
    }


}
