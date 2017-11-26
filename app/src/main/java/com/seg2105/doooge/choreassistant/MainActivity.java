package com.seg2105.doooge.choreassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Login");
    //private final

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        ChildEventListener childEventListener = databaseChore.addChildEventListener(new ChildEventListener() {
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                while (iterator.hasNext()) {
//                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
//                    iterator.next();//discard encrypted: String
//                    iterator.next();//discard userID: long
//                    iterator.next();//discard userName: String
//                    if (isAdmin) {
//
//
//
//                    }
//                }
//            }
//
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                while (iterator.hasNext()) {
//                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
//                    iterator.next();//discard encrypted: String
//                    iterator.next();//discard userID: long
//                    iterator.next();//discard userName: String
//                    if (isAdmin) {
//                        adminExist = true;
//                        allowCreateUser();
//                        System.out.println("Oh..");
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                while (iterator.hasNext()) {
//                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
//                    iterator.next();//discard encrypted: String
//                    iterator.next();//discard userID: long
//                    iterator.next();//discard userName: String
//                    if (isAdmin) {
//                        adminExist = true;
//                        allowCreateUser();
//                    }
//                }
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                Iterator iterator = dataSnapshot.getChildren().iterator();
//                while (iterator.hasNext()) {
//                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
//                    iterator.next();//discard encrypted: String
//                    iterator.next();//discard userID: long
//                    iterator.next();//discard userName: String
//                    if (isAdmin) {
//                        adminExist = true;
//                        allowCreateUser();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}
