package com.seg2105.doooge.choreassistant;

import java.io.Serializable;

/**
 * Created by fredpan on 2017/11/21.
 */

public class PersonRule implements Serializable {
    private String userName;
    private boolean admin;
    private String encrypted;
    private String color;
    private int userID;


    public PersonRule() {
    }

    public PersonRule(String userName, String encrypted, boolean admin, String color, int userID) {
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

    public String getColor() {
        return color;
    }


}
