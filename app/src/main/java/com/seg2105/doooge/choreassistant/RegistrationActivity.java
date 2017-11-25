package com.seg2105.doooge.choreassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.seg2105.doooge.choreassistant.dBUtility.LoginInfoUtility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by fredpan on 2017/11/21.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static ArrayList<Identification> a = new ArrayList<>();
    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Login");
    int ctr = 0;
    ArrayList<Identification> identification = new ArrayList<>();
    private int userID;
    private Boolean isAdmin = false;
    private Button createUser;
    private Button createAdmin;
    private LinearLayout registrationButtonSet;
    private boolean adminExist = false;
    private LoginInfoUtility loginInfoUtility;
    private Object testInter = (Object) 0;

//    boolean isAdmin = (boolean) dataSnapshot.child("admin").getValue();
//    String hashed = (String) dataSnapshot.child("encrypted").getValue();
//    long id = (long) dataSnapshot.child("userID").getValue();
//    String usernamea =  (String) dataSnapshot.child("userName").getValue();
//    OR

//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_info);
        loginInfoUtility = new LoginInfoUtility();
        Random rdm = new Random();
        userID = 000000 + rdm.nextInt(999999);
//        createUser = (Button) findViewById(R.id.createUser);
//        createUser.setOnClickListener(this);
//        createAdmin = findViewById(R.id.createAdmin);
//        registrationButtonSet = findViewById(R.id.registrationButtonSet);
//        createAdmin.setOnClickListener(this);
//        createUser.setClickable(false);//init
//        AdminExistEvaluation();


    }


    //==========
    public ArrayList<Identification> getAllEventsOnFirebase(final ArrayList<Identification> events) {
        DatabaseReference databaseLoginInfo1 = FirebaseDatabase.getInstance().getReference("Login");
        databaseLoginInfo1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("HELLLLO!");

                dataSnapshot = dataSnapshot.child("sample");
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

                Identification identification = new Identification(username, hashed, id, isAdmin, color);

                a.add(identification);
                ctr++;
                // System.out.println(identification == null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        return events;
    }
    //===================


    private void allowCreateUser() {
        System.out.println("COMMMMMMMMMEEEEE ON!!:" + adminExist + "Should equal above");
        // If no admin, require to create one
        if (adminExist) {
            //System.out.println("HERE");
            createUser.setClickable(true);
            adminExist = false;// reset adminExist as false
//            return true;
        } else {
            adminExist = false;
            createUser.setClickable(false);
//            return false;

        }
    }

    public void AdminExistEvaluation() {
        ChildEventListener childEventListener = loginInfoUtility.getDatabaseLoginInfo().addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
                    iterator.next();//discard encrypted: String
                    iterator.next();//discard userID: long
                    iterator.next();//discard userName: String
                    if (isAdmin) {
                        adminExist = true;
                        System.out.println("TEEEEESSSTTTT:" + adminExist);

                        allowCreateUser();

                    }
                }
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
                    iterator.next();//discard encrypted: String
                    iterator.next();//discard userID: long
                    iterator.next();//discard userName: String
                    if (isAdmin) {
                        adminExist = true;
                        allowCreateUser();
                        System.out.println("Oh..");
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
                    iterator.next();//discard encrypted: String
                    iterator.next();//discard userID: long
                    iterator.next();//discard userName: String
                    if (isAdmin) {
                        adminExist = true;
                        allowCreateUser();
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    boolean isAdmin = (boolean) ((DataSnapshot) iterator.next()).getValue();//save admin:boolean
                    iterator.next();//discard encrypted: String
                    iterator.next();//discard userID: long
                    iterator.next();//discard userName: String
                    if (isAdmin) {
                        adminExist = true;
                        allowCreateUser();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void StoreAccountInfo() throws NoSuchAlgorithmException {
        // Storing new username with its related encrypted password, user ID, and isAdmin to database
        EditText obtainedUsername = findViewById(R.id.newUserName);
        EditText obtainedPassword = findViewById(R.id.newPassword);
        String username = String.valueOf(obtainedUsername.getText());
        String password = String.valueOf(obtainedPassword.getText());
        String encrypted = encryption(username, password);
        String color = "Some COLOR TO BE IMPLEMENTED";
        Identification identification = new Identification(username, encrypted, userID, isAdmin, color);
        loginInfoUtility.updateDatabaseLoginInfo(username, identification);
    }

    private String encryption(String username, String pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(
                concatenation(
                        pwd.getBytes(), username.getBytes()
                )
        );
        byte[] encrypted = md.digest();
        encrypted.toString();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < encrypted.length; i++) {
            sb.append(Integer.toString((encrypted[i] & 0xff) + 0x100, 16).toUpperCase().substring(1));
        }
        String result = sb.toString();
        return result;
    }

    private void addChildSafely(DataSnapshot dataSnapshot) {
        System.out.println(dataSnapshot.child("admin"));
        boolean tmpIsAdmin = true;//(boolean) dataSnapshot.child("admin").getValue();
        if (tmpIsAdmin) {
            createUser.setClickable(true);
            try {
                StoreAccountInfo();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            System.out.println("FINAL:            TO BE IMPLEMENTED: Pop up notify success, and redirect to Other page");
        } else {
            createUser.setClickable(false);
            System.out.println("FINAL:            TO BE IMPLEMENT: POP UP: PLZ create admin first");
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.createUser:

                createUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseLoginInfo.addChildEventListener(new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                addChildSafely(dataSnapshot);
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                addChildSafely(dataSnapshot);
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                addChildSafely(dataSnapshot);
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                addChildSafely(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                });
//                if (createUser.isClickable()) {
//                    try {
//                        StoreAccountInfo();
//                    } catch (NoSuchAlgorithmException e) {
//                        e.printStackTrace();
//                    }
//                    allowCreateUser();
//                    System.out.println("TO BE IMPLEMENTED: Pop up notify success, and redirect to Other page");
//                } else {
//                    System.out.println("TO BE IMPLEMENT: POP UP: PLZ create admin first");
//                }
                break;

            case R.id.createAdmin:

                System.out.println("Should be same as below: " + createUser.isClickable());

                AdminExistEvaluation();

                System.out.println("Should be false: ==============" + createUser.isClickable());
                if (isQualified()) {
                    isAdmin = true;
                    System.out.println("TO BE IMPLEMENTED: Pop up asking input admin info, notify success,and redirect to Other page");
                    try {
                        StoreAccountInfo();

                        isAdmin = false;
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("POP UP: Cannot create,remain in this page");
                }

                break;

        }
    }

    public void deleteAdmin() {
        //databaseLoginInfo.removeValue();
        createUser.setClickable(false);
        AdminExistEvaluation();
        //remove

    }

    private Boolean isQualified() {

        return true;
    }

    private byte[] concatenation(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
