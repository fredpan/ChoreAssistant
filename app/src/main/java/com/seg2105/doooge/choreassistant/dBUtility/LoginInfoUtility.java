package com.seg2105.doooge.choreassistant.dBUtility;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seg2105.doooge.choreassistant.PersonRule;

/**
 * Created by fredpan on 2017/11/23.
 */

public class LoginInfoUtility {
    private DatabaseReference databaseLoginInfo;
    private PersonRule personRule;

    public LoginInfoUtility() {
        databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");

    }

    public DatabaseReference getDatabaseLoginInfo() {
        return databaseLoginInfo;
    }

    public void updateDatabaseLoginInfo(String key, PersonRule personRule) {
        databaseLoginInfo.child(key).setValue(personRule);
    }

    public PersonRule getIdentification(DataSnapshot dataSnapshot, String key) {
        boolean isAdmin = (boolean) dataSnapshot.child("isAdmin").getValue();
        String hashed = (String) dataSnapshot.child("encrypted").getValue();
        int id = (int) dataSnapshot.child("userID").getValue();
        String username = (String) dataSnapshot.child("userName").getValue();
        String color = (String) dataSnapshot.child("color").getValue();
        PersonRule result = new PersonRule(username, hashed, isAdmin, color, id);
        return result;
    }

    public PersonRule searchIdentification(final String key) {
        System.out.println("HELLO!");
        databaseLoginInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("HELLLLO!");
                int ctr = 0;
                dataSnapshot = dataSnapshot.child(key);
                System.out.println("INNER:" + dataSnapshot.getValue());
                boolean isAdmin;
                String hashed;
                int id;
                String username;
                String color;
                isAdmin = (boolean) dataSnapshot.child("isAdmin").getValue();
                //System.out.println("1st:"+ (isAdmin));
                hashed = (String) dataSnapshot.child("encrypted").getValue();

                id = (int) dataSnapshot.child("userID").getValue();

                username = (String) dataSnapshot.child("userName").getValue();

                color = (String) dataSnapshot.child("color").getValue();

                // personRule = new PersonRule(username, hashed, isAdmin, color);

                //System.out.println(personRule == null);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return personRule;
    }
}
