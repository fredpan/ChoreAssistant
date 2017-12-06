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
import android.widget.Button;
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
import java.util.List;


public class ChoreEdit extends AppCompatActivity {

    private DatabaseReference databaseLoginInfo;
    private DatabaseReference databaseChores;
    private DatabaseReference databaseReward;

    //stores calendar information if a chore was passed through intent, these will be updated
    private int day, month, year, hour, minute = -1;

    private Chore choreSubmit;
    private PersonRule currentUser;

    private List<PersonRule> personRulesList;
    private List<PersonRule> selectedPersonRuleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_edit);

        Intent intent = getIntent();
        choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");
        currentUser = (PersonRule) intent.getSerializableExtra("currentUser");

        databaseChores      = FirebaseDatabase.getInstance().getReference("chore");
        databaseLoginInfo   = FirebaseDatabase.getInstance().getReference("PersonRule");
        databaseReward      = FirebaseDatabase.getInstance().getReference("Reward");

        userListen();

        if (choreSubmit != null){
            choreFound();
            Button btnDelete = findViewById(R.id.btnDelete);
            btnDelete.setEnabled(true);
        }

    }


    /**
     * Set Class Variables - recieves an array of integers that correspond to
     * slected users from checkbox dialog.
     *
     *
     * @param selectedUserAtIndex
     */
    private void setSelectedPersonRuleList(ArrayList selectedUserAtIndex){

        selectedPersonRuleList= new ArrayList<>();

        for (int i = 0 ; i < selectedUserAtIndex.size() ; i++ ){
            selectedPersonRuleList
                    .add(personRulesList
                            .get( Integer.valueOf(selectedUserAtIndex.get(i).toString() )
                            )
                    );
        }
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * UI Event - Extracts a time from the chore passed through intent
     * then converts the millisecond value of that time into a proper date
     * with year, month, day and time with hour and minute.
     *
     *
     */
    private void choreFound(){
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

        setDate(calYear, calMonth, calDay);
        setTime(calHour, calMinute);
    }


    /**
     * UI Event - Set the text of the textDate field.
     *
     * @param year  calendar year
     * @param month calendar month
     * @param day   calendar day
     */
    private void setDate(int year, int month, int day) {
        TextView textDate = findViewById(R.id.textDate);

        this.day    = day;
        this.month  = month;
        this.year   = year;

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
     * UI Event - Recieves the time in hours and minutes, then uses
     * String format to add missing 0s in front of single digit times
     *
     * @param hour   time in hours
     * @param minute time in minutes
     */
    private void setTime(int hour, int minute) {
        this.hour   = hour;
        this.minute = minute;

        TextView textTime = findViewById(R.id.textTime);
        textTime.setText( String.format("%02d:%02d", hour, minute) );
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Helper Method - creates associations from responsibilites to users and from
     * responsibilities to chores, then adds them to firebase.
     *
     *
     * @param chore the chore that is to be associated
     *
     */
    private void createResponsibilites(Chore chore, int points ) throws NoSuchAlgorithmException {
        for ( PersonRule person : selectedPersonRuleList ){
            Responsibility responsibility = new Responsibility( person.getUserID(), chore.getChoreIdentification() );
            responsibility.setPoints( points );
            person.addResponsibility(responsibility);
            chore.addResponsibility(responsibility);

            databaseLoginInfo.child( person.getUserName() ).setValue(person);
        }

        databaseChores.child(chore.getChoreIdentification() ).setValue(chore);
    }


    /**
     * Helper Method - Removes all associations a chore has to responsibilities and then
     * removes it from firebase.
     *
     *
     * @param chore
     */
    private void scrubResponsibilities( Chore chore ){
        for (Responsibility responsibility : chore.getResponsibilities()) {
            int id = responsibility.getUserID();

            for(PersonRule user : personRulesList){
                if(id == user.getUserID() ){
                    user.deleleteResponsibilityWithID( responsibility.getResponsibilityID() );
                    databaseLoginInfo.child(user.getUserName()).setValue(user);
                }
            }
        }

        databaseChores.child( chore.getChoreIdentification() ).removeValue();
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * OnClick Event - after setting the calendar and extracting the time in millis,
     * we determine whether or not a chore was passed. If a chore was passed, then a
     * method is called to remove all responsibilities from the chore. Afterwards a
     * method call is made to create new responsibilities, then a final method call is
     * made to switch intent to Chore List
     *
     * @param view
     *
     */
    public void btnSubmit_OnClick(View view) throws NoSuchAlgorithmException {
        TextView txtName            = findViewById(R.id.textName);
        TextView txtTime            = findViewById(R.id.textTime);
        TextView txtDate            = findViewById(R.id.textDate);
        TextView txtSelectedUsers   = findViewById(R.id.textSelectUsers);
        TextView txtDescription     = findViewById(R.id.textDescription);
        TextView txtPoints          = findViewById(R.id.textPoints);

        boolean allPass = true;

        try {
            int points = Integer.parseInt(txtPoints.getText().toString().trim());
        } catch (NumberFormatException exNum) {
            allPass = false;
            txtPoints.requestFocus();
            txtPoints.setError("Enter a number.");
            txtPoints.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }

        if (txtName.getText().toString().trim().equals("") ) {
            allPass = false;
            txtName.requestFocus();
            txtName.setError("Enter a name.");
            txtName.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if (txtTime.getText().toString().trim().equals("")){
            allPass = false;
            txtTime.setError("Enter a time.");
            txtTime.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if (txtDate.getText().toString().trim().equals("")){
            allPass = false;
            txtDate.setError("Enter a date.");
            txtDate.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if (txtPoints.getText().toString().trim().equals("")){
            allPass = false;
            txtPoints.requestFocus();
            txtPoints.setError("Enter amount of points.");
            txtPoints.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if ((selectedPersonRuleList != null) && (selectedPersonRuleList.size() == 0)) {
            allPass = false;
            txtSelectedUsers.setError("Please assign a user.");
            txtSelectedUsers.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }


        if (allPass) {
            String name         = txtName.getText().toString().trim();
            String description  = txtDescription.getText().toString().trim();
            Calendar calChore   = Calendar.getInstance();

            calChore.set(year, month, day, hour, minute);
            long millis = calChore.getTimeInMillis();

            if (choreSubmit != null) {
                scrubResponsibilities(choreSubmit);
            }

            Chore chore = new Chore(name, description, millis);
            //chore.setPoints(Integer.parseInt(txtPoints.getText().toString().trim()));

            createResponsibilites( chore, Integer.parseInt(txtPoints.getText().toString().trim())  );

            choreListShow();
        }
    }


    /**
     * OnClick Event - Displays a conformitation to the user about thier choice
     * then, if selected, deletes the chore from firebase and removes all assiciations
     *
     *
     * @param view
     */
    public void btnDelete_OnClick(View view){

        AlertDialog.Builder deleteConfirm = new AlertDialog.Builder( this );
        deleteConfirm.setTitle("Delete");
        deleteConfirm.setMessage("Are you sure you want to delete?");
        deleteConfirm.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                for (Responsibility responsibility : choreSubmit.getResponsibilities()){
                    int responsibilityUserID = responsibility.getUserID();

                    for ( PersonRule user : personRulesList ){
                        if ( responsibilityUserID == user.getUserID() ){
                            user.deleleteResponsibilityWithID(responsibility.getResponsibilityID());
                            databaseLoginInfo.child(user.getUserName()).setValue(user);
                        }
                    }
                }

                databaseChores.child(choreSubmit.getChoreIdentification()).removeValue();

                choreListShow();
            }
        });

        deleteConfirm.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        deleteConfirm.show();
    }


    /**
     * OnClick Event - Resets the textview back to orginial state if a warning had occured.
     * Then calls the method that displays the checkbox dialog.
     *
     * @param view
     */
    public void textSelectUsers_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtSelect = findViewById(R.id.textSelectUsers);
        txtSelect.setError(null);
        selectUsers();
    }


    /**
     * OnClick Event - Resets the textview back to orginial state if a warning had occured.
     * Then calls the method that displays the date picker dialog
     *
     * @param view
     */
    public void textDate_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtDate = findViewById(R.id.textDate);
        txtDate.setError(null);
        datePick();
    }


    /**
     * OnClick Event - Resets the textview back to orginial state if a warning had occured.
     * Then calls the method that displays the time picker dialog
     *
     * @param view
     */
    public void textTimne_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtTime = findViewById(R.id.textTime);
        txtTime.setError(null);
        timePick();
    }


    /**
     * OnClick Event - Resets the textview back to orginial state if a warning had occured.
     *
     *
     * @param view
     */
    public void textName_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtName = findViewById(R.id.textName);
        txtName.setError(null);
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Dialog - displays a checklist dialog and lists all users that were selected.
     *
     * NOTE: the following site was referenced in the construction of this method
     * https://developer.android.com/reference/android/app/AlertDialog.Builder.html
     *
     */
    private void selectUsers(){
        //used in the creating of userList, list of all selected users
        final String[] users            = new String[personRulesList.size()];
        final ArrayList selectedUsers   = new ArrayList();

        for (int i = 0; i <personRulesList.size() ; i++){
            users[i] = personRulesList.get(i).getUserName();
        }

        //pass personRulist list to a string Array for functionality in alert dialog
        AlertDialog.Builder userList = new AlertDialog.Builder( ChoreEdit.this );
        userList.setTitle("Select who should complete the chore.");

        //detect which users were selected for a task
        userList.setMultiChoiceItems(users, null, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    selectedUsers.add(which);
                } else {
                    selectedUsers.remove(Integer.valueOf(which));
                }
            }
        });

        //add all selected users to a list and pass it to an instance variable
        userList.setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //build a new list
                StringBuilder sb    = new StringBuilder();

                for (int i = 0; i < selectedUsers.size();i++){
                    sb.append(users[ Integer.valueOf( selectedUsers.get(i).toString() ) ]);
                    if (i+1 < selectedUsers.size() ) { sb.append(", "); }
                }

                TextView textSelect = findViewById(R.id.textSelectUsers);
                textSelect.setText( sb.toString() );

                setSelectedPersonRuleList(selectedUsers);
            }
        });

        userList.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        userList.show();
    }


    /**
     * Dialog - Displays an Error to the user about an issue that was encounter and
     * then returns them to the previous intent.
     *
     *
     */
    private void unhandledEvent(String warningTitle, String warningDescription){

        AlertDialog.Builder deleteConfirm = new AlertDialog.Builder( this );
        deleteConfirm.setTitle(warningTitle);
        deleteConfirm.setMessage(warningDescription);
        deleteConfirm.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                choreListShow();
            }
        });

        deleteConfirm.show();
    }


    /**
     * Dialog - displays a time picker dialog and calls a method to display the time in a text
     *
     * NOTE: the following site was referenced in the construction of this method
     * https://developer.android.com/reference/android/app/TimePickerDialog.html
     *
     */
    private void timePick() {

        Calendar cal    = Calendar.getInstance();
        int hour        = cal.get(Calendar.HOUR_OF_DAY);
        int minute      = cal.get(Calendar.MINUTE);

        TimePickerDialog temp = new TimePickerDialog(this, timeListen, hour, minute, false);
        temp.show();
    }


    /**
     * Dialog - displays a date picker dialog and calls a method to display the date in a text
     * a minimum date is set to inhibit the currentUser from assigning any user a chore with
     * a date set in the past.
     *
     * NOTE: the following sites were referenced in the construction of this method
     * https://developer.android.com/reference/android/app/DatePickerDialog.html
     * https://developer.android.com/reference/java/util/Calendar.html
     *
     */
    private void datePick() {

        //year, month, and day have to be set before calling or the date is very wrong
        Calendar cal    = Calendar.getInstance();
        int year        = cal.get(Calendar.YEAR);
        int month       = cal.get(Calendar.MONTH);
        int day         = cal.get(Calendar.DAY_OF_MONTH);
        int hour        = cal.get(Calendar.HOUR_OF_DAY);
        int minute      = cal.get(Calendar.MINUTE);

        cal.set(year,month,day,hour,minute);

        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.getDatePicker().setMinDate( cal.getTimeInMillis() - 1000 );
        temp.show();
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Listen Event - recreates and fills an array of all the current users
     * If a chore was passed through intent a second array is created and
     * filled with all the users that have a relationship to that chores
     * responsibility
     *
     *
     */
    private void userListen() {
        selectedPersonRuleList = new ArrayList<>();

        databaseLoginInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear and rebuild personRule list
                personRulesList = new ArrayList<>();
                for (DataSnapshot personRoleInstance : dataSnapshot.getChildren()) {
                    PersonRule user = personRoleInstance.getValue(PersonRule.class);
                    if ( !user.isAdmin() ){
                        personRulesList.add(user);
                    }


                    //personRulesList.add(personRoleInstance.getValue(PersonRule.class));
                }
                if (choreSubmit == null) return ;

                StringBuilder userID = new StringBuilder();
                List<Responsibility> responsibilities = choreSubmit.getResponsibilities();

                for (int i = 0; i < responsibilities.size() ; i++){
                    int tempID = responsibilities.get(i).getUserID();

                    for (PersonRule user : personRulesList) {
                        if (tempID == user.getUserID() ) {
                            userID.append( user.getUserName() );
                            selectedPersonRuleList.add(user);
                        }
                    }
                    if (i+1 < responsibilities.size() ){userID.append(", ");}
                }
                TextView textSelectedUsers = findViewById(R.id.textSelectUsers);
                textSelectedUsers.setText( userID.toString() );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Listener - displays a time picker and calls a function to both display and set the time
     *
     * NOTE: the following site was referenced in the construction of this method
     * //https://developer.android.com/reference/android/app/TimePickerDialog.OnTimeSetListener.html
     *
     */
    private TimePickerDialog.OnTimeSetListener timeListen = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTime(hourOfDay, minute);
        }
    };


    /**
     * Listener - display a date picker and calls a function to both display and set the date
     *
     * NOTE: the following site was referenced in the construction of this method
     * https://developer.android.com/reference/android/app/DatePickerDialog.OnDateSetListener.html
     *
     */
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //From article: the selected month (0-11 for compatibility with MONTH), so add 1...
            setDate(year, month, dayOfMonth);
        }

    };


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Intent - changes intent to ChoreList and passes the current user
     *
     */
    private void choreListShow(){
        Intent intent = new Intent(ChoreEdit.this, ChoreList.class);
        intent.putExtra("currentUser",currentUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}

