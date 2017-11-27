package com.seg2105.doooge.choreassistant;

/**
 * Created by dustin on 11/22/17.
 */

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;


public class ChoreEdit extends AppCompatActivity {

    private int day = -1;
    private int month = -1;
    private int year = -1;
    private int hour =-1;
    private int minute=-1;
    private Chore choreSubmit;

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

        Spinner spin = findViewById(R.id.spnType);
        String[] options = {
                "",
                "General Cleaning",
                "Yard work",
                "Laundry",
                "Pets and Plants",
                "Cooking"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        spin.setAdapter(adapter);



        Intent intent = getIntent();
        choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");

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

    //public void btnTime(View view){
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


    //https://developer.android.com/reference/android/app/TimePickerDialog.html
    private void timePick() {

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);

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

            Chore chore = new Chore(name,description,millis, 99);


            Intent intent = new Intent(ChoreEdit.this, ChoreList.class);
            intent.putExtra("SUBMIT", chore);
            startActivity(intent);


        }

    }

}