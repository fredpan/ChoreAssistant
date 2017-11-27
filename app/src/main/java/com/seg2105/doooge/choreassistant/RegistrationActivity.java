package com.seg2105.doooge.choreassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by fredpan on 2017/11/21.
 */

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static ArrayList<PersonRule> a = new ArrayList<>();
    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    ArrayList<Integer> userIDs = new ArrayList<>();
    private int userID;

    private Boolean isAdmin = false;
    private Button createUser;
    private Button createAdmin;
    private Button blue;
    private Button green;
    private Button yellow;
    private Button violet;
    private Button red;
    private int color;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init
        setContentView(R.layout.registration);


        createUser = findViewById(R.id.createUser);
        createUser.setOnClickListener(this);
        createUser.setClickable(false);

        createAdmin = findViewById(R.id.createAdmin);
        createAdmin.setOnClickListener(this);

        blue = findViewById(R.id.blue_4169E1);
        blue.setOnClickListener(this);
        green = findViewById(R.id.green_32CD32);
        green.setOnClickListener(this);
        yellow = findViewById(R.id.yellow_FFA500);
        yellow.setOnClickListener(this);
        violet = findViewById(R.id.violet_8A2BE2);
        violet.setOnClickListener(this);
        red = findViewById(R.id.red_FF0000);
        red.setOnClickListener(this);

        databaseLoginInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean enableCreateNormalUser = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting product
                    PersonRule personRule = postSnapshot.getValue(PersonRule.class);
                    userIDs.add(personRule.getUserID());
                    if (personRule.isAdmin()) {
                        enableCreateNormalUser = enableCreateNormalUser || true;
                    }

                }

                Random rd = new Random();
                userID = 000000 + rd.nextInt(999999);
                while (isUnique(userID)) {
                    userID = 000000 + rd.nextInt(999999);
                }

                if (enableCreateNormalUser) {
                    createUser.setClickable(true);
                    System.out.println("createUser has been enabled");
                } else {
                    createUser.setClickable(false);
                    System.out.println("createUser has been disabled");
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.createUser:
                if (createUser.isClickable()) {
                    try {
                        storeAccountInfo();
                        createUser.setClickable(false);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please create admin first !!!!", Toast.LENGTH_SHORT).show();
                    System.out.println("TO BE IMPLEMENT: POP UP: PLZ CREATE ADMIN FIRST!!!!!");
                }
                break;

            case R.id.createAdmin:

                try {
                    isAdmin = true;
                    storeAccountInfo();
                    isAdmin = false;
                    createUser.setClickable(false);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.blue_4169E1:
                color = getResources().getColor(R.color.blue);
                break;
            case R.id.green_32CD32:
                color = getResources().getColor(R.color.green);
                break;
            case R.id.yellow_FFA500:
                color = getResources().getColor(R.color.yellow);
                System.out.println("=========" + color);
                break;
            case R.id.violet_8A2BE2:
                color = getResources().getColor(R.color.violet);
                break;
            case R.id.red_FF0000:
                color = getResources().getColor(R.color.red);
                break;

        }
    }


    private void storeAccountInfo() throws NoSuchAlgorithmException {
        // Storing new username with its related encrypted password, user ID, and isAdmin to database
        EditText obtainedUsername = findViewById(R.id.newUserName);
        EditText obtainedPassword = findViewById(R.id.newPassword);
        String username = String.valueOf(obtainedUsername.getText());
        String password = String.valueOf(obtainedPassword.getText());
        String encrypted = IdentificationUtility.generateIdentification(username, password);
        //color = "Some COLOR TO BE IMPLEMENTED";
        PersonRule personRule = new PersonRule(username, encrypted, isAdmin, color, userID);
        databaseLoginInfo.child(username).setValue(personRule);
    }


    private boolean isUnique(int num) {
        Iterator<Integer> iterator = userIDs.iterator();

        while (iterator.hasNext()) {
            int existedUserNum = iterator.next();
            if (existedUserNum == num) {

                return true;
            }
        }
        return false;
    }
}
