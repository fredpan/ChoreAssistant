package com.seg2105.doooge.choreassistant;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fredpan on 2017/11/30.
 */

public class Reward implements Serializable {

    //private PersonRule personRule;
    private String userName;
    private String rewardName;
    //private int userID;
    private int points;
    private List<Responsibility> responsibilities;
    //private ArrayList<Responsibility> responsibilities;


/*
    public int getUserID(){
        return userID;
    }

    public void setUserID(int userID){
        this.userID = userID;
    }
    */

    public List<Responsibility> getResponsibilities(){
        return responsibilities;
    }

    public void setResponsibilities(List<Responsibility> responsibilities){
        this.responsibilities = responsibilities;
    }

    public void addResponsibility(Responsibility responsibility){
        responsibilities.add(responsibility);
    }




    public Reward() {
        //For Firebase
        responsibilities = new ArrayList<>();
    }

    /*
    public Reward(final PersonRule personRule) {
        this.personRule = personRule;

        //responsibilities = new ArrayList<>();
        points = 0;

        this.userName = personRule.getUserName();

    }
    */

    public int getPoints(){
        return points;
    }

    public void setPoints(int points){
        this.points = points;
    }

/*
    public PersonRule getPersonRule() {
        return personRule;
    }

    public void setPersonRule(PersonRule personRule) {
        this.personRule = personRule;
    }
   */

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*
    public void setResponsibilities(ArrayList<Responsibility> responsibilities) {
        this.responsibilities = responsibilities;
    }

    public void addResponsibility(Responsibility responsibility){
        responsibilities.add(responsibility);
    }

    public void removeResponsibility(Responsibility responsibility){
        responsibilities.remove(responsibility);
    }


    public ArrayList<Responsibility> getResponsibilities(){
        ArrayList<Responsibility> tempResponsibilities = responsibilities;
        return tempResponsibilities;
    }
    */

    public String getRewardName(){
        return rewardName;
    }

    public void setRewardName(String rewardName){
        this.rewardName = rewardName;
    }

    public String getUserName(){
        return userName;
    }

}
