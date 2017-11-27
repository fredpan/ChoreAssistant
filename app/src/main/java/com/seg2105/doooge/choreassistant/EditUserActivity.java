package com.seg2105.doooge.choreassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;

/**
 * Created by user on 2017/11/26.
 */

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {
    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    private Button update;
    private Button delete;
    private PersonRule personRule;
    private String newUserName;
    private String newPassword;
    private String encrypted;
    private int color;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_info);
        Intent intent = getIntent();
        personRule = (PersonRule) intent.getSerializableExtra("userInfo");

        update = findViewById(R.id.updateUserInfo);
        update.setOnClickListener(this);

        delete = findViewById(R.id.deleteUser);
        delete.setOnClickListener(this);

        userName = findViewById(R.id.newUserName);
        password = findViewById(R.id.newPassword);
        userName.setText(personRule.getUserName());

        try {
            encrypted = IdentificationUtility.generateIdentification(newUserName, newPassword);
            color = 00000;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

        //obtain new data
        newUserName = String.valueOf(userName.getText());
        System.out.println(newUserName);
        newPassword = String.valueOf(password.getText());

        switch (view.getId()) {


            case R.id.updateUserInfo:
//                Map<String, Object> updateUserName = new HashMap<>();
//                System.out.println("personRule.getUserName()");
//                System.out.println("/PersonRule/"+personRule.getUserName()+"/userName/");
//                updateUserName.put("/PersonRule/"+personRule.getUserName()+"/userName/",newUserName);
//                databaseLoginInfo.child(personRule.getUserName()).updateChildren(updateUserName);
//                System.out.println("HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                PersonRule newPersonRule = new PersonRule(newUserName, encrypted, personRule.isAdmin(), color, personRule.getUserID());
                databaseLoginInfo.child(personRule.getUserName()).removeValue();
                databaseLoginInfo.child(newPersonRule.getUserName()).setValue(newPersonRule);

                break;
            case R.id.deleteUser:
                databaseLoginInfo.child(personRule.getUserName()).removeValue();
                break;


        }


    }
}
