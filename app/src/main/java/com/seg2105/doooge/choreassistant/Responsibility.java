package com.seg2105.doooge.choreassistant;

/**
 * Created by dustin on 11/25/17.
 */

public class Responsibility {

    private Chore chore;
    private PersonRule person;

    public void Responsibility(Chore chore){
        this.chore = chore;
    }

    public Chore getChore(){
        return chore;
    }

    public void setPerson(PersonRule person){
        this.person = person;
    }

    public PersonRule getPerson(){
        return person;
    }

    public void setChore(Chore chore){
        this.chore = chore;
    }

}
