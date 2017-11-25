package com.seg2105.doooge.choreassistant;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by user on 2017/11/22.
 */

public class showDetailDialog extends AppCompatActivity {
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choredetail_dialog);
        initialData();

        final ImageButton finish_button = (ImageButton) findViewById(R.id.finishButton);
        finish_button.setOnClickListener(new View.OnClickListener() {
            boolean click = false;

            @Override
            public void onClick(View view) {
                click = !click;
                if (click) {

                    finish_button.setBackgroundResource(R.drawable.finish_button);
                    Toast.makeText(getApplicationContext(), "Congraduation, You finish !!!!", Toast.LENGTH_SHORT).show();
                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            finish();
                            t.cancel();
                        }
                    }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.

                } else {

                    finish_button.setBackgroundResource(R.drawable.finish_button2);
                }


            }
        });


    }


    public void initialData() {
        TextView choreName = (TextView) findViewById(R.id.printChoreName);
        choreName.setText("Clean the Garage");
        TextView time = (TextView) findViewById(R.id.printTime);
        time.setText("2017/11/22" + "\n" + "18:00");
        TextView description = (TextView) findViewById(R.id.printDescription);
        description.setText("Please be careful !!!");
        TextView reward = (TextView) findViewById(R.id.printReward);
        reward.setText("You will get 10 dollars!");


    }

    public void showDialog() {

        dialog = new Dialog(this);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.choredetail_dialog, null);
        dialog.setContentView(dialogView);
        dialog.setTitle("ChoreDetail");


        final ImageButton imageButton = (ImageButton) dialogView.findViewById(R.id.finishButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            boolean click = false;

            @Override
            public void onClick(View view) {
                click = !click;
                if (click) {

                    imageButton.setBackgroundResource(R.drawable.finish_button);
                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            dialog.dismiss();
                            t.cancel();
                        }
                    }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.

                } else {

                    imageButton.setBackgroundResource(R.drawable.finish_button2);
                }
            }
        });


        dialog.show();


    }

    public void closeDialog(View view) throws Exception {
        try {
            finish();
        } catch (Exception e) {
        }
    }


}
