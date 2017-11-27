package com.seg2105.doooge.choreassistant;

import java.security.NoSuchAlgorithmException;

/**
 * Created by dustin on 11/25/17.
 */

public class Responsibility {


    private int userID;
    private String choreIdentification;
    private String responsibilityID;
    private Chore chore;


    //############### UPDATE IF USERROLE CLASS IS IMPLEMENTED ###############
    private PersonRule personRule;
    //#######################################################################


    public Responsibility() {
        //Keep for Firebase only
    }

    //
    public Responsibility(int userID, String choreIdentification) throws NoSuchAlgorithmException {
        this.userID = userID;
        this.choreIdentification = choreIdentification;
        this.responsibilityID = IdentificationUtility.generateIdentification(Integer.toString(userID), choreIdentification);
    }

    public void setChore(Chore chore){
        this.chore = chore;
    }

    public Chore getChore(){
        return chore;
    }



    //############### UPDATE IF USERROLE CLASS IS IMPLEMENTED ###############
    public void setPersonRule(PersonRule personRule){
        this.personRule = personRule;
    }

    public PersonRule getPersonRule(){
        return personRule;
    }
    //#######################################################################



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
