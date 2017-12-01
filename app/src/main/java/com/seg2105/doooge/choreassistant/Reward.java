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

    //private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    private PersonRule personRule;
    private String userName;
    private String rewardName;

//    public DatabaseReference getDatabaseLoginInfo() {
//        return databaseLoginInfo;
//    }

    public PersonRule getPersonRule() {
        return personRule;
    }

    public void setPersonRule(PersonRule personRule) {
        this.personRule = personRule;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setResponsibilities(ArrayList<Responsibility> responsibilities) {
        this.responsibilities = responsibilities;
    }

    private String rewardDescription;
    private ArrayList<Responsibility> responsibilities;
    private boolean userAnnounced;
    private boolean adminAnnounced;

    public Reward() {
        //For Firebase
        responsibilities = new ArrayList<>();
    }

    public Reward(final PersonRule personRule) {
        this.personRule = personRule;

        responsibilities = new ArrayList<>();

        this.userName = personRule.getUserName();
        /*
        databaseLoginInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                responsibilities.clear();
                for (DataSnapshot instanceOfResponsibility : dataSnapshot.child(personRule.getUserName()).child("responsibilities").getChildren()) {
                    Responsibility responsibility = instanceOfResponsibility.getValue(Responsibility.class);
                    responsibilities.add(responsibility);
                    System.out.println("=================" + instanceOfResponsibility.getValue());
                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

    }

    public void addResponsibility(Responsibility responsibility){
        responsibilities.add(responsibility);
    }

    public void removeResponsibility(Responsibility responsibility){
        responsibilities.remove(responsibility);
    }

    /*
    public boolean isAchieved() {

        databaseLoginInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                responsibilities.clear();
                for (DataSnapshot instanceOfResponsibility : dataSnapshot.child(personRule.getUserName()).child("responsibilities").getChildren()) {
                    Responsibility responsibility = instanceOfResponsibility.getValue(Responsibility.class);
                    responsibilities.add(responsibility);
                    System.out.println("=================" + instanceOfResponsibility.getValue());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for (Responsibility responsibility : responsibilities) {
            if (!responsibility.isComplete()) {
                return false;
            }
        }
        return true;
    }
*/

    public ArrayList<Responsibility> getResponsibilities(){
        ArrayList<Responsibility> tempResponsibilities = responsibilities;
        return tempResponsibilities;
    }

    public String getRewardName(){
        return rewardName;
    }

    public void setRewardName(String rewardName){
        this.rewardName = rewardName;
    }

    public String getRewardDescription(){
        return rewardDescription;
    }

    public boolean getUserAnnounced(){
        return userAnnounced;
    }

    public void setUserAnnounced(boolean flag){
        this.userAnnounced = flag;
    }

    public boolean getAdminAnnounced(){
        return adminAnnounced;
    }

    public void setAdminAnnounced(boolean flag){
        this.adminAnnounced = flag;
    }

    public void setRewardDescription(String rewardDescription){
        this.rewardDescription = rewardDescription;
    }

    public String getUserName(){
        return userName;
    }

}
