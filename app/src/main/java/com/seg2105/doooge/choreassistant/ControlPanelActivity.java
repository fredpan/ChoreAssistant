package com.seg2105.doooge.choreassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by fredpan on 2017/11/24.
 */

public class ControlPanelActivity extends AppCompatActivity {

    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    ArrayList<PersonRule> personRules = new ArrayList<>();
    private ListView controlPanelListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_panel);
        controlPanelListView = findViewById(R.id.controlPanelListView);


        databaseLoginInfo.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot personRoleInstance : dataSnapshot.getChildren()) {
                            PersonRule personRule = personRoleInstance.getValue(PersonRule.class);
                            personRules.add(personRule);

                        }
                        UserInfoAdapter userInfoAdapter = new UserInfoAdapter(personRules, ControlPanelActivity.this);
                        controlPanelListView.setAdapter(userInfoAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );



//        databaseLoginInfo.addValueEventListener(new ValueEventListener() {
//            ArrayList<PersonRule> identificationsList = new ArrayList<>();
//
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                for (DataSnapshot userInfoInstance : dataSnapshot.getChildren()) {
////                    PersonRule personRule = userInfoInstance.getValue(PersonRule.class);
////                    identificationsList.add(personRule);
////                }
////
////                UserInfoAdapter userInfoAdapter = new UserInfoAdapter(identificationsList, ControlPanelActivity.this);
////                controlPanelListView.setAdapter(userInfoAdapter);
////                identificationsList.clear();
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////
////            }
////        });


//        //FAKE!!!!!
//        PersonRule a = new PersonRule("test", "nejrfnlkfn", false, getResources().getColor(R.color.blue), 12345);
//        PersonRule b = new PersonRule("Fred cooks the dinner", "nejrfnlkfn", false, getResources().getColor(R.color.green), 12345);
//        PersonRule c = new PersonRule("Dustin washes the dishes", "nejrfnlkfn", false, getResources().getColor(R.color.yellow), 12345);
//        PersonRule d = new PersonRule("Vison2 cleans the drive way", "nejrfnlkfn", false, getResources().getColor(R.color.red), 12345);
//        //PersonRule e = new PersonRule(" Miguel A. Garzón","nejrfnlkfn",123456,true,"#999999");
//        personRule = a;
//        ArrayList<PersonRule> identificationsList = new ArrayList<>();
//        identificationsList.add(a);
//        identificationsList.add(b);
//        identificationsList.add(c);
//        identificationsList.add(d);

//      );

        // creat a listener for addUser button
        Button addUserButton = this.findViewById(R.id.addUser);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ControlPanelActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void btnCreateReward_OnClick(View view){
        Intent intent = new Intent(ControlPanelActivity.this, RewardEdit.class);
        startActivity(intent);
    }


}
