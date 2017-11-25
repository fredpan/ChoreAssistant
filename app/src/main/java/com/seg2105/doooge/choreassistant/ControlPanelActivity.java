package com.seg2105.doooge.choreassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by fredpan on 2017/11/24.
 */

public class ControlPanelActivity extends AppCompatActivity {

    ListView controlPanelListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_panel);
        controlPanelListView = findViewById(R.id.controlPanelListView);

        //TEST
//        Identification a = new Identification("Vison1","nejrfnlkfn",123456,false,"#5ff718");
//        Identification b = new Identification("Dustin","nejrfnlkfn",123456,false,"#2e8af4");
//        Identification c = new Identification("Fred","nejrfnlkfn",123456,false,"#e63e3e");
//        Identification d = new Identification("Vison2","nejrfnlkfn",123456,false,"#666666");
//        Identification e = new Identification(" Miguel A. Garzón","nejrfnlkfn",123456,true,"#999999");
//        ArrayList<Identification> identificationsList = new ArrayList<>();
//        identificationsList.add(e);identificationsList.add(a);identificationsList.add(b);identificationsList.add(c);identificationsList.add(d);
//
//        System.out.println("========");
//        UserListAdapter userListAdapter = new UserListAdapter(identificationsList,ControlPanelActivity.this);
//        controlPanelListView.setAdapter(userListAdapter);
        //PASSED!!!

        //FAKE!!!!!
        Identification a = new Identification("Vison1 cleans the garage", "nejrfnlkfn", 123456, false, "#5ff718");
        Identification b = new Identification("Fred cooks the dinner", "nejrfnlkfn", 123456, false, "#2e8af4");
        Identification c = new Identification("Dustin washes the dishes", "nejrfnlkfn", 123456, false, "#e63e3e");
        Identification d = new Identification("Vison2 cleans the drive way", "nejrfnlkfn", 123456, false, "#666666");
        //Identification e = new Identification(" Miguel A. Garzón","nejrfnlkfn",123456,true,"#999999");
        ArrayList<Identification> identificationsList = new ArrayList<>();
        identificationsList.add(a);
        identificationsList.add(b);
        identificationsList.add(c);
        identificationsList.add(d);

        System.out.println("========");
        RewardAdapter rewardAdapter = new RewardAdapter(identificationsList, ControlPanelActivity.this);
        controlPanelListView.setAdapter(rewardAdapter);
    }
}
