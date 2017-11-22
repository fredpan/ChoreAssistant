package com.seg2105.doooge.choreassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by fredpan on 2017/11/21.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference databaseLoginInfo;
    private int userID;
    private Boolean isAdmin = false;
    private Button createUser;
    private Button createAdmin;
    private boolean adminExist = false;

    //((HashMap) dataSnapshot.child(key).getValue()).get("admin");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Login");
        Random rdm = new Random();
        userID = 000000 + rdm.nextInt(999999);
        createUser = (Button) findViewById(R.id.createUser);
        createUser.setOnClickListener(this);
        createAdmin = (Button) findViewById(R.id.createAdmin);
        createAdmin.setOnClickListener(this);
        AdminExist();

    }

    private boolean allowCreateUser() {
        // If no admin, require to create one
        AdminExist();
        if (adminExist) {
            System.out.println("HERE");
            createUser.setClickable(true);
            return true;
        } else {
            createUser.setClickable(false);
            return false;
        }
    }

    public void AdminExist() {

        ChildEventListener childEventListener = databaseLoginInfo.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    Iterator temp = dataSnapshot.getChildren().iterator();
                    while (temp.hasNext()){
                        System.out.println(temp.next());
                    }
                    //System.out.println("HELLO: "+dataSnapshot.getChildren().iterator().next().getValue());
//                    boolean isadmin = (Boolean) ((HashMap) dataSnapshot.child(key).getValue()).get("admin") ;
//                    if (isadmin) {
//                        createUser.setClickable(true);
//                        System.out.println(adminExist + "!!!!!!!!!!!!!!!!!"+createUser.getLinksClickable());
//                        break;
//                    }
//                    System.out.println("Set creatUser as FALSE");
                    createUser.setClickable(false);
                }

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
//                    boolean isadmin = (Boolean) ((HashMap) dataSnapshot.child(key).getValue()).get("admin") ;
//                    if (isadmin) {
//                        createUser.setClickable(true);
//                        System.out.println(adminExist + "!!!!!!!!!!!!!!!!!"+createUser.getLinksClickable());
//                        break;
//                    }
//                    System.out.println("Set creatUser as FALSE");
                    createUser.setClickable(false);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();

//                    boolean isadmin = (Boolean) ((HashMap) dataSnapshot.child(key).getValue()).get("admin") ;
//                    if (isadmin) {
//                        createUser.setClickable(true);
//                        System.out.println(adminExist + "!!!!!!!!!!!!!!!!!"+createUser.getLinksClickable());
//                        break;
//                    }
//                    System.out.println("Set creatUser as FALSE");
                    createUser.setClickable(false);
                }


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
//                    boolean isadmin = (Boolean) ((HashMap) dataSnapshot.child(key).getValue()).get("admin") ;
//                    if (isadmin) {
//                        adminExist = true;
//                        System.out.println(adminExist + "!!!!!!!!!!!!!!!!!");
//                        break;
//                    }
//
//                    System.out.println("Set creatUser as FALSE");
                    createUser.setClickable(false);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void StoreAccountInfo() throws NoSuchAlgorithmException {
        // Storing new username with its related encrypted password, user ID, and isAdmin to database
        EditText obtainedUsername = (EditText) findViewById(R.id.newUserName);
        EditText obtainedPassword = (EditText) findViewById(R.id.newPassword);
        String username = String.valueOf(obtainedUsername.getText());
        String password = String.valueOf(obtainedPassword.getText());
        String encrypted = encryption(username, password);
        Identification identification = new Identification(username, encrypted, userID, isAdmin);
        databaseLoginInfo.child(username).setValue(identification);
        System.out.println("===============");
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createUser:
                //if (allowCreateUser()) {
                    try {
                        StoreAccountInfo();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    System.out.println("TO BE IMPLEMENTED: Pop up notify success, and redirect to Other page");
               // } else {
                //    System.out.println("TO BE IMPLEMENT: POP UP: PLZ create admin first");
               // }
                break;
            case R.id.createAdmin:
                if (isQualified()) {
                    isAdmin = true;
                    System.out.println("TO BE IMPLEMENTED: Pop up asking input admin info, notify success,and redirect to Other page");
                    try {
                        StoreAccountInfo();
               //         createUser.setClickable(true);
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
