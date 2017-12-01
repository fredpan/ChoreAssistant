package com.seg2105.doooge.choreassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;



/**
 * Created by dustin on 11/22/17.
 */

public class ChoreList extends AppCompatActivity {

    //DatabaseReference databaseResponsibilities;
    DatabaseReference databaseChores;
    DatabaseReference databaseUsers;

    private PersonRule currentUser;

    //stores the current date
    private int day, month, year;
    private Calendar cal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_list);

        Intent intent   = getIntent();
        currentUser     = (PersonRule) intent.getSerializableExtra("currentUser");

        //setting up calendar
        cal     = Calendar.getInstance();
        day     = cal.get(Calendar.DAY_OF_MONTH);
        month   = cal.get(Calendar.MONTH);
        year    = cal.get(Calendar.YEAR);

        setDate(year, month, day);

        //initiate databases and their appropriate listeners
        //databaseResponsibilities = FirebaseDatabase.getInstance().getReference("responsibility");
        databaseChores  = FirebaseDatabase.getInstance().getReference("chore");
        databaseUsers   = FirebaseDatabase.getInstance().getReference("PersonRule");

        choreListen();
        userListen();

    }


    /**
     * Listener for new chores, display is based on the users priveledge
     * or whether or not the chore is for that specific person
     */
    public void choreListen() {
        //making this callable so update to the UI can happen
        databaseChores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Chore chore = postSnapshot.getValue(Chore.class);

                    //tinkering but case should be avoided
                    //if (chore.getResponsibilities() == null) throw new UnassignedChoreException;

                    if (currentUser.isAdmin()) {
                        displayChore(chore);
                    } else if (chore.getResponsibilities() != null) {

                        //Testing if any of the available chores are for this particular user.
                        for (Responsibility responsibility : chore.getResponsibilities()) {
                            if (responsibility == null) break;

                            String temp1 = responsibility.getUserID();
                            String temp2 = currentUser.getUserName();

                            if ((temp1 != null) && (temp2 != null) && (temp1.equals(temp2))) {
                                displayChore(chore);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * possible dead method
     */
    public void responsibilityListen() {

    }


    /**
     * Check whether or not the user has sufficient privledge to view edit abilities
     * and disable them if not
     */
    public void userListen() {
        // check if admin, if not ,set invisible for add button and edit button
        if (!currentUser.isAdmin()) {
            Button add = findViewById(R.id.btnAdd);
            add.setVisibility(View.INVISIBLE);
        } else {
            Button add = findViewById(R.id.btnAdd);
            add.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Change intents and pass the current user on button action
     * @param view this
     */
    public void add_OnClick(View view) {
        Intent intent = new Intent(ChoreList.this, ChoreEdit.class); //switch homepage to edit chore page
        intent.putExtra("currentUser",currentUser);
        startActivity(intent);
    }


    /**
     * Creates a gridlayout and adds the new layout to choreView
     * the content that will be displayed is based on the chore
     *
     * @param chore the chore that has been assigned to, or created by, the current user.
     */
    private void displayChore (Chore chore){
        //extract Calendar data from chore
        Calendar tempCal        = Calendar.getInstance();
        tempCal.setTimeInMillis( chore.getTimeInMillis() );

        int choreHours          = tempCal.get(Calendar.HOUR);
        int choreMinute         = tempCal.get(Calendar.MINUTE);
        int choreYear           = tempCal.get(Calendar.YEAR);
        int choreMonth          = tempCal.get(Calendar.MONTH);
        int choreDay            = tempCal.get(Calendar.DAY_OF_MONTH);

        //if chores day doesn't match the current displayed day then exit
        if ((choreYear != year) || (choreMonth != month) || (choreDay != day)) {
            return;
        }

        LinearLayout linearView = findViewById(R.id.choreView);

        GridLayout gridLayout   = new GridLayout(this);
        Point point             = new Point();                  //required for to get display size

        //create text views and add text to them
        TextView text1          = textTest(chore.getChoreName(),18);
        TextView text2          = textTest( String.format("%02d:%02d", choreHours, choreMinute), 14);
        TextView text3          = textTest(chore.getDescription(), 14);

        //set the layout
        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(2);
        text1.setGravity(Gravity.LEFT);
        text2.setGravity(Gravity.TOP | Gravity.LEFT);
        text3.setGravity(Gravity.LEFT);
        getWindowManager().getDefaultDisplay().getSize(point);

        //set the text views
        text1.setWidth(point.x - 160);
        text2.setWidth( 160 );
        text3.setWidth(point.x - 160);
        text1.setTypeface(Typeface.DEFAULT_BOLD);
        text2.setTypeface(null,Typeface.ITALIC);
        text3.setTypeface(null,Typeface.BOLD_ITALIC);

        text1.setTextColor(Color.BLACK);

        //add text views to new grid layout
        gridLayout.addView(text1);
        gridLayout.addView(text2);
        gridLayout.addView(text3);

        gridLayout.setPadding(0,30,0,30);

        //tag the chore for passing through intents on edit
        gridLayout.setTag(chore);

        // Create a listener for touch screen events;
        gridLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //switch intents and pass the chore tagged to the event
                Toast.makeText(getBaseContext(), " " + ((Chore) view.getTag()).getChoreName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoreList.this, showDetailDialog.class);
                intent.putExtra("SUBMIT", (Chore) view.getTag());
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);

            }
        });

        //Create a listener for long touch screen events;
        gridLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //switch intents and pass both the chore, which is tagged to the event, and the current user
                Intent intent = new Intent(ChoreList.this, ChoreEdit.class);
                intent.putExtra("SUBMIT", (Chore) view.getTag());
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);

                return true;

            }
        });

        //finally, place the gridview on layout to be displayed
        linearView.addView(gridLayout);

    }

    /**
     * Creates new textviews to display in new gridlayout of choreView
     *
     * @param text text to be inserted into textview
     * @param textSize size of the font of the text
     * @return
     */
    private TextView textTest(String text, int textSize){
        TextView tempText = new TextView(this);

        tempText.setTextSize(textSize);
        tempText.setText(text);
        return tempText;
    }


    /**
     * method called when the text dispay the date is clicked
     * @param view
     */
    public void textDate_OnClick(View view) {
        datePick();
    }


    /**
     * method called when the left pointing chevron is clicked
     * @param view
     */
    public void imgDateUp_OnClick(View view) {
        scrubChoreView();
        buildNewDate(1);
        choreListen();
    }


    /**
     * method called when the right pointing chevron is clicked
     * @param view
     */
    public void imgDateDown_OnClick(View view) {
        scrubChoreView();
        buildNewDate(-1);
        choreListen();
    }


    /**
     * Sets the class varibles to this date and, if required, refreshes the ui
     *
     * @param year sets the new year
     * @param month sets the new month
     * @param day sets the new day
     */
    private void updateDate(int year, int month, int day){

        //if the user selected the all ready displayed date, do nothing
        if ((year == this.year) && (month == this.month) && (day == this.day)) {
            return;
        }

        cal.set(year,month,day);

        this.year   = year;
        this.month  = month;
        this.day    = day;

        choreListen();
    }


    /**
     * used to either add or subtract X amount of days from the current day
     *
     * @param upDown amount in days to shift the calendar date by, can be negative or positive
     */
    private void buildNewDate(int upDown) {
        cal.add(Calendar.DATE, upDown);

        year    = cal.get(Calendar.YEAR);
        month   = cal.get(Calendar.MONTH);
        day     = cal.get(Calendar.DAY_OF_MONTH);

        updateDate(year, month, day);
        setDate(year, month, day);
    }


    /**
     * clears the ui of all currently displayed chores
     */
    private void scrubChoreView(){
        LinearLayout choreView = findViewById(R.id.choreView);
        choreView.removeAllViews();
    }

    /**
     * Displays the date as a readable text to textDate
     *
     * @param year the year the calendar is to be set to
     * @param month the month the calendar is to be set to
     * @param day the day the calendar is to be set to
     */
    private void setDate(int year, int month, int day) {

        TextView textDate = findViewById(R.id.textDate);

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
                "December"};

        textDate.setText(monthString[month] + " " + day + ", " + year);
    }


    /**
     * displays a date picker dialog
     */
    private void datePick() {
        //https://developer.android.com/reference/android/app/DatePickerDialog.html
        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
    }

    /**
     * listener for the date picker dialog
     */
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //the selected month (0-11 for compatibility with MONTH)
            scrubChoreView();  //clear ui
            updateDate(year, month, dayOfMonth); //adjust class variables
            setDate(year, month, dayOfMonth);  //display the text
        }
    };


}
