package com.seg2105.doooge.choreassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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


        //FAKE!!!!!
        PersonRule a = new PersonRule("Vison1 cleans the garage", "nejrfnlkfn", false, "#5ff718", 12345);
        PersonRule b = new PersonRule("Fred cooks the dinner", "nejrfnlkfn", false, "#2e8af4", 12345);
        PersonRule c = new PersonRule("Dustin washes the dishes", "nejrfnlkfn", false, "#e63e3e", 12345);
        PersonRule d = new PersonRule("Vison2 cleans the drive way", "nejrfnlkfn", false, "#666666", 12345);
        //PersonRule e = new PersonRule(" Miguel A. Garzón","nejrfnlkfn",123456,true,"#999999");
        ArrayList<PersonRule> identificationsList = new ArrayList<>();
        identificationsList.add(a);
        identificationsList.add(b);
        identificationsList.add(c);
        identificationsList.add(d);

        RewardAdapter rewardAdapter = new RewardAdapter(identificationsList, ControlPanelActivity.this);
        controlPanelListView.setAdapter(rewardAdapter);

    }

    public void onClick(View view) {

    }

}
