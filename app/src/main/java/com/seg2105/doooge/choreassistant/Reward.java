package com.seg2105.doooge.choreassistant;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by fredpan on 2017/11/30.
 */

public class Reward {

    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    private PersonRule personRule;
    private ArrayList<Responsibility> responsibilities;

    public Reward() {
        //For Firebase
    }

    public Reward(final PersonRule personRule) {
        this.personRule = personRule;
        responsibilities = new ArrayList<>();

        databaseLoginInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

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

    }

    public boolean isAchieved() {
        for (Responsibility responsibility : responsibilities) {
            if (!responsibility.isComplete()) {
                return false;
            }
        }
        return true;
    }

}
