package com.seg2105.doooge.choreassistant.dBUtility;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seg2105.doooge.choreassistant.Identification;

/**
 * Created by fredpan on 2017/11/23.
 */

public class LoginInfoUtility {
    private DatabaseReference databaseLoginInfo;
    private Identification identification;

    public LoginInfoUtility() {
        databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Login");

    }

    public DatabaseReference getDatabaseLoginInfo() {
        return databaseLoginInfo;
    }

    public void updateDatabaseLoginInfo(String key, Identification identification) {
        databaseLoginInfo.child(key).setValue(identification);
    }

    public Identification getIdentification(DataSnapshot dataSnapshot, String key) {
        boolean isAdmin = (boolean) dataSnapshot.child("admin").getValue();
        String hashed = (String) dataSnapshot.child("encrypted").getValue();
        long id = (long) dataSnapshot.child("userID").getValue();
        String username = (String) dataSnapshot.child("userName").getValue();
        String color = (String) dataSnapshot.child("color").getValue();
        Identification result = new Identification(username, hashed, id, isAdmin, color);
        return result;
    }

    public Identification searchIdentification(final String key) {
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
                long id;
                String username;
                String color;
                isAdmin = (boolean) dataSnapshot.child("admin").getValue();
                //System.out.println("1st:"+ (isAdmin));
                hashed = (String) dataSnapshot.child("encrypted").getValue();

                id = (long) dataSnapshot.child("userID").getValue();

                username = (String) dataSnapshot.child("userName").getValue();

                color = (String) dataSnapshot.child("color").getValue();

                identification = new Identification(username, hashed, id, isAdmin, color);

                //System.out.println(identification == null);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return identification;
    }
}
