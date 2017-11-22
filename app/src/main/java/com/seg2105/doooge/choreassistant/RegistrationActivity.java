package com.seg2105.doooge.choreassistant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by fredpan on 2017/11/21.
 */

public class RegistrationActivity extends AppCompatActivity {
    private DatabaseReference databaseLoginInfo;
    private DatabaseReference databaseRoleInfo;
    private int userID;
    private boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Login");
        databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Role");
        Random rdm = new Random();
        userID = 000000 + rdm.nextInt(999999);
    }

    public void submit(View view) throws NoSuchAlgorithmException {
        // Storing new username with its related password
        EditText username = (EditText) findViewById(R.id.newUserName);
        EditText password  = (EditText) findViewById(R.id.newPassword);
        String encrypted = encryption(String.valueOf(username.getText()), String.valueOf(password.getText()));
        databaseLoginInfo.child(Integer.toString(userID)).setValue(encrypted);



    }

    private String encryption(String username, String pwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(
                concatenation(
                        pwd.getBytes(),username.getBytes()
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

    private byte[] concatenation(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
