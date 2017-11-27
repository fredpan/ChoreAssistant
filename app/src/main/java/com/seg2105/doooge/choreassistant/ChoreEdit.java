package com.seg2105.doooge.choreassistant;

/**
 * Created by dustin on 11/22/17.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;


public class ChoreEdit extends AppCompatActivity {

    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    private final DatabaseReference databaseChore = FirebaseDatabase.getInstance().getReference("Chore");
    private final DatabaseReference databaseResponsibility = FirebaseDatabase.getInstance().getReference("Responsibility");

    //stores calendar information if a chore was passed through intent, these will be updated
    private int day = -1;
    private int month = -1;
    private int year = -1;
    private int hour =-1;
    private int minute=-1;
    private Chore choreSubmit;

    private PersonRule currentUser;

    private ArrayList<String> personRulesList = new ArrayList<>();
    private String[] userList;


    //https://developer.android.com/reference/android/app/TimePickerDialog.OnTimeSetListener.html
    private TimePickerDialog.OnTimeSetListener timeListen = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTime(hourOfDay, minute);
        }
    };
    //https://developer.android.com/reference/android/app/DatePickerDialog.OnDateSetListener.html
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //From article: the selected month (0-11 for compatibility with MONTH), so add 1...
            setDate(year, month, dayOfMonth);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_edit);

        Intent intent = getIntent();
        choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");
        currentUser = (PersonRule) intent.getSerializableExtra("currentUser");

        if (choreSubmit != null){
            TextView txtCaption     = findViewById(R.id.textCaption);
            TextView txtName        = findViewById(R.id.textName);
            TextView txtDescription = findViewById(R.id.textDescription);

            txtCaption.setText("Edit Chore");
            txtName.setText(choreSubmit.getChoreName());
            txtDescription.setText(choreSubmit.getDescription());

            Calendar tempCal    = Calendar.getInstance();
            long millis         = choreSubmit.getTimeInMillis();

            tempCal.setTimeInMillis(millis);

            int calYear         = tempCal.get(Calendar.YEAR);
            int calMonth        = tempCal.get(Calendar.MONTH);
            int calDay          = tempCal.get(Calendar.DAY_OF_MONTH);
            int calHour         = tempCal.get(Calendar.HOUR);
            int calMinute       = tempCal.get(Calendar.MINUTE);

            setDate(calYear, calMonth,calDay);
            setTime(calHour, calMinute);

        }


        databaseLoginInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                personRulesList = new ArrayList<>();
                for (DataSnapshot personRoleInstance : dataSnapshot.getChildren()) {
                    String personRule = (String) personRoleInstance.child("userName").getValue();
                    personRulesList.add(personRule);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Displays alert dialog of users in the database
     *
     * @param view current
     */
    public void selectedUsers(View view){
        //https://developer.android.com/reference/android/app/AlertDialog.Builder.html

        //used in the creating of userList, list of all selected users
        final String[] users            = new String[personRulesList.size()];
        final ArrayList selectedUsers   = new ArrayList();

        //pass personRulist list to a string Array for functionality in alert dialog
        for (int i = 0; i < personRulesList.size(); i++){
            users[i] = personRulesList.get(i);
        }



        AlertDialog.Builder userList = new AlertDialog.Builder(this);
        userList.setTitle("Select who should complete the chore.");

        //detect which users were selected for a task
        userList.setMultiChoiceItems(users, null, new DialogInterface.OnMultiChoiceClickListener() {

            //auto-filled... when finishing above line
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    selectedUsers.add(which);
                } else {
                    selectedUsers.remove(which);
                }
            }
        });

        //add all selected users to a list and pass it to an instance variable
        userList.setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //build a new list
                String[] tempUsers  = new String[selectedUsers.size()];
                StringBuilder sb    = new StringBuilder();

                for (int i = 0; i < selectedUsers.size();i++){
                    tempUsers[i] = users[ Integer.valueOf( selectedUsers.get(i).toString() ) ];
                    sb.append(users[ Integer.valueOf( selectedUsers.get(i).toString() ) ]);

                    if (i+1 < selectedUsers.size() ) { sb.append(", "); }
                }

                TextView textSelect = findViewById(R.id.textSelectUsers);
                textSelect.setText( sb.toString() );

                setUserList(tempUsers);

            }
        });

        userList.show();

    }


    private void setUserList(String[] userList){
        this.userList = userList;
    }


    public void textDate_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtDate = findViewById(R.id.textDate);
        txtDate.setError(null);
        datePick();
    }


    public void textName_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtName = findViewById(R.id.textName);
        txtName.setError(null);
    }

    public void textTimne_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtTime = findViewById(R.id.textTime);
        txtTime.setError(null);
        timePick();
    }

    /**
     * Set the text of the textDate field.
     *
     * @param year  calendar year
     * @param month calendar month
     * @param day   calendar day
     */
    private void setDate(int year, int month, int day) {
        TextView textDate = findViewById(R.id.textDate);

        this.day = day;
        this.month = month;
        this.year = year;

        String[] monthString = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December" };


        textDate.setText( monthString[month] +" " + day + ", " + year);
    }

    /**
     * Set the text of the textTime field.
     *
     * @param hour   time in hours
     * @param minute time in minutes
     */
    private void setTime(int hour, int minute) {
        TextView textTime = findViewById(R.id.textTime);
        this.hour = hour;
        this.minute = minute;
        textTime.setText( String.format("%02d:%02d", hour, minute) );
    }



    private void timePick() {
        //https://developer.android.com/reference/android/app/TimePickerDialog.html

        Calendar cal    = Calendar.getInstance();
        int hour        = cal.get(Calendar.HOUR);
        int minute      = cal.get(Calendar.MINUTE);

        TimePickerDialog temp = new TimePickerDialog(this, timeListen, hour, minute, false);
        temp.show();
    }

    //https://developer.android.com/reference/android/app/DatePickerDialog.html
    private void datePick() {

        //year, month, and day have to be set before calling or the date is very very very very very wrong
        //https://developer.android.com/reference/java/util/Calendar.html
        Calendar cal    = Calendar.getInstance();
        int year        = cal.get(Calendar.YEAR);
        int month       = cal.get(Calendar.MONTH);
        int day         = cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
    }

    public void btnSubmit_OnClick(View view) throws NoSuchAlgorithmException {

        TextView txtName = findViewById(R.id.textName);
        TextView txtDescription = findViewById(R.id.textDescription);
        TextView txtTime = findViewById(R.id.textTime);
        TextView txtDate = findViewById(R.id.textDate);

        Boolean allPass = true;

        if (txtName.getText().toString().equals("")) {
            allPass = false;
            txtName.setError("Enter a name.");
            txtName.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if (hour == -1){
            allPass = false;
            txtTime.setError("Enter a time.");
            txtTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if (year == -1){
            allPass = false;
            txtDate.setError("Enter a date.");
            txtDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }

        if (allPass){
            String name = txtName.getText().toString();
            String description = txtDescription.getText().toString();

            Calendar calChore = Calendar.getInstance();
            calChore.set(year,month,day,hour,minute);

            long millis = calChore.getTimeInMillis();
            Chore chore = new Chore(name, description, millis);

            databaseChore.child(chore.getChoreIdentification()).setValue(chore);//update the Chore
            databaseChore.child(choreSubmit.getChoreIdentification()).removeValue();


            Intent intent = new Intent(ChoreEdit.this, ChoreList.class);
            //intent.putExtra("SUBMIT", chore);
            //intent.putExtra("USERS", userList);

            intent.putExtra("currentUser",currentUser);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}

