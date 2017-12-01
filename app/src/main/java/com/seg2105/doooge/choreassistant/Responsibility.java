package com.seg2105.doooge.choreassistant;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dustin on 11/25/17.
 */

public class Responsibility implements Serializable {

    //private int userID;
    private int userID;
    private String choreIdentification;
    private String responsibilityID;
    private Chore chore;
    private boolean isComplete;


    private PersonRule personRule;


    public Responsibility() {
        //Keep for Firebase only
    }

    //
    //public Responsibility(int userID, String choreIdentification) throws NoSuchAlgorithmException {
    public Responsibility(int userID, String choreIdentification) throws NoSuchAlgorithmException {
        //this.userID = userID;
        this.userID = userID;
        this.choreIdentification = choreIdentification;
        this.responsibilityID = IdentificationUtility.generateIdentification(String.valueOf(userID), choreIdentification);

        //this.responsibilityID = IdentificationUtility.generateIdentification(Integer.toString(userID), choreIdentification);
    }

    public Chore getChore(){
        return chore;
    }

    public void setChore(Chore chore) {
        this.chore = chore;
    }

    public PersonRule getPersonRule(){
        return personRule;
    }

    public void setPersonRule(PersonRule personRule) {
        this.personRule = personRule;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

/*
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
*/

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


    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
