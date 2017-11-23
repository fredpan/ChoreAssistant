package com.seg2105.doooge.choreassistant;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dustin on 11/22/17.
 */

public class ChoreList extends AppCompatActivity {

    int day;
    int month;
    int year;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_list);

        Calendar cal = Calendar.getInstance();

        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        setDate(day,month,year);
    }

    private void date_OnClick() {
        datePick();
    }

    private void setDate(int year, int month, int day) {
        TextView textDate = (TextView) findViewById(R.id.textDate);

        Calendar cal = Calendar.getInstance();
        String monthString = cal.getDisplayName(month,Calendar.LONG, Locale.getDefault());
        textDate.setText(monthString + " " + day + ", " + year);
    }

    private void datePick() {

        //year, month, and day have to be set before calling or the date is very very very very very wrong
        //https://developer.android.com/reference/java/util/Calendar.html
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
        //temp.setOnDateSetListener(tempListen);
    }

    //https://developer.android.com/reference/android/app/DatePickerDialog.OnDateSetListener.html
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //From article: the selected month (0-11 for compatibility with MONTH), so add 1...
            setDate(year, month + 1, dayOfMonth);
        }

    };

}
