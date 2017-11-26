package com.seg2105.doooge.choreassistant;


import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

/**
 * Created by dustin on 11/25/17.
 */

public class Chore implements Serializable {

    private String description;
    private String choreName;
    private Long timeInMillis;
    private String choreIdentification="TO BE IMPLEMENTED";
    private int choreID;
    private Boolean complete;

    public Chore(String choreName, String description, long timeInMillis, int choreID) throws NoSuchAlgorithmException {
        this.choreName      = choreName;
        this.description    = description;
        this.timeInMillis   = timeInMillis;
        this.choreID        = choreID;
        complete = false;

        this.choreID = choreID;
        choreIdentification = IdentificationUtility.generateIdentification(choreName, String.valueOf(choreID), description);

        complete = false;
        //generateChoreCharacIdentification();
    }

    public long getTimeInMillis(){
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis){
        this.timeInMillis = timeInMillis;
    }

    public int getChoreID() {
        return choreID;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getChoreName(){
        return choreName;
    }

    public void setChoreName(String choreName){
        this.choreName = choreName;
    }

    public Boolean getComplete(){
        return complete;
    }

    public void setComplete(boolean value) {
        this.complete = value;
    }

    public String getChoreIdentification() {
        return choreIdentification;
    }

}
