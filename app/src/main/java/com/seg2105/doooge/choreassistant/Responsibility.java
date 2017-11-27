package com.seg2105.doooge.choreassistant;

import java.security.NoSuchAlgorithmException;

/**
 * Created by dustin on 11/25/17.
 */

public class Responsibility {


    private int userID;
    private String choreIdentification;
    private String responsibilityID;

    public Responsibility() {
        //Keep for Firebase only
    }

    //
    public Responsibility(int userID, String choreIdentification) throws NoSuchAlgorithmException {
        this.userID = userID;
        this.choreIdentification = choreIdentification;
        this.responsibilityID = IdentificationUtility.generateIdentification(Integer.toString(userID), choreIdentification);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getChoreIdentification() {
        return choreIdentification;
    }

    public void setChoreIdentification(String choreIdentification) {
        this.choreIdentification = choreIdentification;
    }

    public String getResponsibilityID() {
        return responsibilityID;
    }

    public void setResponsibilityID(String responsibilityID) {
        this.responsibilityID = responsibilityID;
    }


}
