package com.seg2105.doooge.choreassistant;

/**
 * Created by dustin on 11/25/17.
 */

public class Responsibility {

    private int userId;
    private String choreIdentification;

    public Responsibility(int userId, String choreIdentification ){
        this.userId = userId;
        this.choreIdentification = choreIdentification;
    }

    public int getUserId() {
        return userId;
    }

    public String getChoreIdentification() {
        return choreIdentification;
    }

    /*
    public Chore getChore(){
        return chore;
    }


    public PersonRule getPerson(){
        return person;
    }
    */
}
