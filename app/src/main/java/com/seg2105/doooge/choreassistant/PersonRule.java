package com.seg2105.doooge.choreassistant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredpan on 2017/11/21.
 */

public class PersonRule implements Serializable {
    private String userName;
    private boolean admin;
    private String encrypted;
    private int color;
    private int userID;

    private List<Responsibility> responsibilities;

    public PersonRule() {
        responsibilities = new ArrayList<Responsibility>();
    }

    public PersonRule(String userName, String encrypted, boolean admin, int color, int userID) {
        this.userName = userName;
        this.userID = userID;
        this.admin = admin;
        this.encrypted = encrypted;
        this.color = color;
        this.responsibilities = new ArrayList<Responsibility>();

    }

    public boolean hasResponsibilities() {
        return responsibilities.size() > 0;
    }

    public boolean addResponsibility(Responsibility responsibility) {
        boolean added = false;
        if (responsibilities.contains(responsibility)) { return false; }

        PersonRule existingPerson = responsibility.getPersonRule();
        boolean isNewPerson = existingPerson != null && !this.equals(existingPerson);
        if (isNewPerson) {
            responsibility.setPersonRule(this);
        } else {
            responsibilities.add(responsibility);
        }
        added = true;
        return added;
    }

    public boolean removeResponsibility(Responsibility responsibility) {
        boolean removed = false;

        if (!this.equals(responsibility.getChore())) {
            responsibilities.remove(responsibility);
            removed = true;
        }
        return removed;
    }

    public void deleleteResponsibilityWithID(String responsibilityID){
        if ( (responsibilities == null) || (responsibilities.size() == 0)  ) return;

        for (Responsibility responsibility : responsibilities){
            String id = responsibility.getResponsibilityID();
            if (id == null) break;

            if(id.equals(responsibilityID)){
                this.removeResponsibility(responsibility);
                return;
            }
        }


    }

    public List<Responsibility> getResponsibilities() {
        List<Responsibility> newResponsibilities = responsibilities;
        return newResponsibilities;
    }

    public int numberOfResponsibilities() {
        return responsibilities.size();
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
