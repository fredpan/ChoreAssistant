package com.seg2105.doooge.choreassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private Button blue;
    private Button green;
    private Button yellow;
    private Button violet;
    private Button red;
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

        blue = findViewById(R.id.edit_blue);
        blue.setOnClickListener(this);
        green = findViewById(R.id.edit_green);
        green.setOnClickListener(this);
        yellow = findViewById(R.id.edit_yellow);
        yellow.setOnClickListener(this);
        violet = findViewById(R.id.edit_violet);
        violet.setOnClickListener(this);
        red = findViewById(R.id.edit_red);
        red.setOnClickListener(this);



        userName = findViewById(R.id.newUserName);
        password = findViewById(R.id.newPassword);
        userName.setText(personRule.getUserName());
        color = personRule.getColor();



    }

    @Override
    public void onClick(View view) {

        //obtain new data
        newUserName = String.valueOf(userName.getText());
        newPassword = String.valueOf(password.getText());
        try {
            encrypted = IdentificationUtility.generateIdentification(newUserName, newPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        switch (view.getId()) {


            case R.id.updateUserInfo:
//                Map<String, Object> updateUserName = new HashMap<>();
//                System.out.println("personRule.getUserName()");
//                System.out.println("/PersonRule/"+personRule.getUserName()+"/userName/");
//                updateUserName.put("/PersonRule/"+personRule.getUserName()+"/userName/",newUserName);
//                databaseLoginInfo.child(personRule.getUserName()).updateChildren(updateUserName);
//                System.out.println("HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                if (newUserName.equals("")) {
                    userName.setError("Enter a name.");
                    userName.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
                } else if (newPassword.equals("")) {
                    password.setError("Enter a password.");
                    password.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
                } else {
                    PersonRule newPersonRule = new PersonRule(newUserName, encrypted, personRule.isAdmin(), color, personRule.getUserID());
                    databaseLoginInfo.child(personRule.getUserName()).removeValue();
                    databaseLoginInfo.child(newPersonRule.getUserName()).setValue(newPersonRule);
                    Toast.makeText(getApplicationContext(), "update success !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditUserActivity.this, ControlPanelActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

                break;
            case R.id.deleteUser:
                databaseLoginInfo.child(personRule.getUserName()).removeValue();
                Toast.makeText(getApplicationContext(), "delete success !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditUserActivity.this, ControlPanelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.edit_blue:
                color = getResources().getColor(R.color.blue);
                Toast.makeText(getApplicationContext(), "choose blue success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_green:
                color = getResources().getColor(R.color.green);
                Toast.makeText(getApplicationContext(), "choose green success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_yellow:
                color = getResources().getColor(R.color.yellow);
                Toast.makeText(getApplicationContext(), "choose yellow success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_violet:
                color = getResources().getColor(R.color.violet);
                Toast.makeText(getApplicationContext(), "choose violet success !", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_red:
                color = getResources().getColor(R.color.red);
                Toast.makeText(getApplicationContext(), "choose red success !", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
