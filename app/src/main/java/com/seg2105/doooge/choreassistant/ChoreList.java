package com.seg2105.doooge.choreassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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

    DatabaseReference databaseChores;
    DatabaseReference databaseUsers;
    DatabaseReference databaseReward;

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
        databaseChores  = FirebaseDatabase.getInstance().getReference("chore");
        databaseUsers   = FirebaseDatabase.getInstance().getReference("PersonRule");
        databaseReward  = FirebaseDatabase.getInstance().getReference("Reward");

        choreListen();
        userListen();
        rewardListen();
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     *
     *
     *
     */


    /**
     * Helper Method - test for completion of reward.
     *
     *
     */
    private void rewardAvailableCheck(){
        //for(Reward reward :  )
    }


    /**
     * Helper Method - receives either a negative or positive integer, (representing days),
     * that is added or subtracted from the current date. Functions are then called to set
     * the class variables and display the result.
     *
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
     * Helper Method - creates a new text view and assigns it the attributes that
     * were passed to the method. The textboxes are used on a grid layout of the
     * choreView to display information about a chore.
     *
     *
     * @param text text to be inserted into textview
     * @param textSize size of the font of the text
     * @return
     */
    private TextView createTextView(String text, int textSize){
        TextView tempText = new TextView(this);

        tempText.setTextSize(textSize);
        tempText.setText(text);
        return tempText;
    }


    /**
     * Helper Method - checks the current users privledge, if the user admin the
     * button that changes intent to ChoreEdit is made visibile, otherwise, it is
     * hidden from view.
     *
     *
     */
    public void userListen() {
        if (!currentUser.isAdmin()) {
            Button add = findViewById(R.id.btnAdd);
            add.setVisibility(View.INVISIBLE);
        } else {
            Button add = findViewById(R.id.btnAdd);
            add.setVisibility(View.VISIBLE);
        }
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * OnClick Event - changes the intent to ChoreEdit and passes ChoreEdit the
     * current user
     *
     *
     * @param view this
     */
    public void add_OnClick(View view) {
        Intent intent = new Intent(ChoreList.this, ChoreEdit.class); //switch homepage to edit chore page
        intent.putExtra("currentUser",currentUser);
        startActivity(intent);
    }


    /**
     * OnClick Event - makes a method call to a date picker dialog to obtain
     * user input about the date they want to switch to.
     *
     *
     * @param view
     */
    public void textDate_OnClick(View view) {
        datePick();
    }


    /**
     * OnClick Event - first the chores that are currently being displayed are cleared
     * from the UI. The next method is then called to increment the date, once that is
     * complete, a call to the chore listener is made to pull from fire base the chores
     * of the current date and place them on the UI.
     *
     *
     * @param view
     */
    public void imgDateUp_OnClick(View view) {
        scrubChoreView();
        buildNewDate(1);
        choreListen();
    }


    /**
     * OnClick Event - first the chores that are currently being displayed are cleared
     * from the UI. The next method is then called to decrement the date, once that is
     * complete, a call to the chore listener is made to pull from fire base the chores
     * of the current date and place them on the UI.
     *
     *
     * @param view
     */
    public void imgDateDown_OnClick(View view) {
        scrubChoreView();
        buildNewDate(-1);
        choreListen();
    }


    /**
     * OnClick Event (Or Highly Related To) - recieves the date as integer representation
     * of the day, month, and year. If those values match the current day nothing
     * happens; If they do not, the class variables are set and chore listener
     * is called to pull from fire base the chores of the current date and
     * place them on the UI
     *
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


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * UI Event | OnClick Event - checks that chore passed to the method has a date
     * that matches the currently set date, if not the method returns.
     * A grid layout is then constructed and a method call is made to create textviews
     * that are inserted onto the grid layout. The chore is tagged to that layout and a
     * listener is set. If the listener is activated the tagged chored will be passed
     * through intent. Lastly, the newly constructed grid view is placed onto the ChoreView.
     *
     *
     * @param chore the chore that has been assigned to, or created by, the current user.
     */
    private void displayChore (Chore chore){
        //extract Calendar data from chore
        Calendar tempCal        = Calendar.getInstance();
        tempCal.setTimeInMillis( chore.getTimeInMillis() );

        int choreHours          = tempCal.get(Calendar.HOUR_OF_DAY);
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
        TextView text1          = createTextView(chore.getChoreName(),18);
        TextView text2          = createTextView( String.format("%02d:%02d", choreHours, choreMinute), 14);
        TextView text3          = createTextView(chore.getDescription(), 14);

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

        for (Responsibility responsibility : chore.getResponsibilities()){
            if (responsibility.isComplete()){
                text1.setPaintFlags( text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
                text2.setPaintFlags( text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
                text3.setPaintFlags( text1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG );
            }
        }

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
     * UI Event - removes all the views currently placed on the choreview.
     *
     *
     */
    private void scrubChoreView(){
        LinearLayout choreView = findViewById(R.id.choreView);
        choreView.removeAllViews();
    }


    /**
     * UI Event - Receives a day, month, year as integer values and converts
     * those values into a string and displays the resultant text to the user.
     *
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


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Dialog - displays a date picker dialog and passes the user information
     * to a method for displaying the result
     *
     * Note: this site was referenced in the construction of this method
     * https://developer.android.com/reference/android/app/DatePickerDialog.html
     *
     */
    private void datePick() {
        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Listener - if the user has selected a date, it calls a method to clear the
     * choreView of all the currently displayed chores. Next, a method is called to
     * set the class variables to the selected date. Finally, the last method is called
     * to display the selected date as text to the user.
     *
     *
     */
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //the selected month (0-11 for compatibility with MONTH)
            scrubChoreView();  //clear ui
            updateDate(year, month, dayOfMonth); //adjust class variables
            setDate(year, month, dayOfMonth);  //display the text
        }
    };


    /**
     * Listener -
     *
     *
     *
     */
    private void rewardListen(){
        databaseReward.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren() ){
                    Reward reward = postSnapshot.getValue(Reward.class);
                    boolean passed = true;
                    for ( Responsibility responsibility : reward.getResponsibilities() ){
                        if (!responsibility.isComplete()){
                            passed = false;
                        }

                    }
                    if (passed && (currentUser.isAdmin()) ) {
                        Toast.makeText(getBaseContext(), reward.getUserName() +
                                " has me the requirements for " + reward.getRewardName(), Toast.LENGTH_LONG  );
                        reward.setAdminAnnounced(true);
                        databaseReward.child(reward.getUserName()).setValue(reward);
                        //break;
                    }
                    if ( passed && currentUser.getUserName().equals(reward.getUserName())){
                        Toast.makeText(getBaseContext(),
                                "Good Work! You have me the requirements for " +
                                        reward.getRewardName(), Toast.LENGTH_LONG);
                        reward.setUserAnnounced(true);
                        databaseReward.child(reward.getUserName()).setValue(reward);
                        //break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Listener - grabs all the chores currently in firebase, if the current user
     * has admin privledge it is displayed; otherwise, only if the chore has been
     * assigned to the current user will the chore be displayed.
     *
     *
     */
    public void choreListen() {
        //making this callable so update to the UI can happen
        databaseChores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Chore chore = postSnapshot.getValue(Chore.class);

                    if (currentUser.isAdmin()) {
                        displayChore(chore);
                    } else if (chore.getResponsibilities() != null) {

                        //Testing if any of the available chores are for this particular user.
                        for (Responsibility responsibility : chore.getResponsibilities()) {
                            if (responsibility == null) break;

                            if ( responsibility.getUserID() == currentUser.getUserID() ){
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



}
