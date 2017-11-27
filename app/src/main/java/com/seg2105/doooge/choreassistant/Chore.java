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

    private Boolean complete;

    public Chore(String choreName, String description, long timeInMillis) throws NoSuchAlgorithmException {
        this.choreName      = choreName;
        this.description    = description;
        this.timeInMillis   = timeInMillis;
        complete = false;
        choreIdentification = IdentificationUtility.generateIdentification(choreName, String.valueOf(timeInMillis), description);

        complete = false;
    }

    public long getTimeInMillis(){
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis){
        this.timeInMillis = timeInMillis;
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
