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
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by dustin on 11/22/17.
 */

public class ChoreList extends AppCompatActivity {

    private final String CHORE_RED      = "#ffff4444";
    private final String CHORE_PURPLE   = "#ffaa66cc";
    private final String CHORE_ORANGE   = "#ffff8800";
    private final String CHORE_GREEN    = "#ff99cc00";
    private final String CHORE_BLUE     = "#ff0099cc";
    private static Chore choreSubmit;
    DatabaseReference databaseChore = FirebaseDatabase.getInstance().getReference("Responsibility");
    DatabaseReference databaseInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    private int day;
    private int month;
    private int year;
    private Calendar cal;
    private PersonRule testRule;

    private Responsibility responsibility;
    //DatabaseReference databaseChore = FirebaseDatabase.getInstance().getReference("Chore");

    //https://developer.android.com/reference/android/app/DatePickerDialog.OnDateSetListener.html
    private DatePickerDialog.OnDateSetListener tempListen = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            //the selected month (0-11 for compatibility with MONTH)
            updateDate(year, month, dayOfMonth);
            setDate(year, month, dayOfMonth);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chore_list);

        //setting up calendar
        cal     = Calendar.getInstance();
        day     = cal.get(Calendar.DAY_OF_MONTH);
        month   = cal.get(Calendar.MONTH);
        year    = cal.get(Calendar.YEAR);

        setDate(year,month,day);


        //############################## FUNCTION FOR SAMPLE CHORES####################
        //displaySampleChores();


        //SAMPLE Responsibility
        Responsibility chore1 = new Responsibility(123, "Resp1: This is the Chore Identification");
        Responsibility chore2 = new Responsibility(321, "Resp2: This is the Chore Identification");
        Responsibility chore3 = new Responsibility(213, "Resp3: This is the Chore Identification");
        databaseChore.child("Resp Identification1").setValue(chore1);
        databaseChore.child("Resp Identification2").setValue(chore2);
        databaseChore.child("Resp Identification3").setValue(chore3);


        databaseChore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (long i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    System.out.println("=====================" + (dataSnapshot.child("Resp Identification1").child("userId").getValue()));

                    //GET THE CORRESPONDING USERINFO AND CHOREINFO


                    System.out.println("++++++++++++++++++++++" + databaseInfo.child("admin"));

                    //=============================================
                    //GridLayout temp = layoutTest(Long.toString((long) dataSnapshot.child("Resp Identification1").child("userId").getValue()), "TIME", "Name"); //, draw[i%5]);

                    //temp.setTag("chore" + i);
                    //
                    // linearView.addView(temp);

                }

//                for (long i = 0; i < dataSnapshot.getChildrenCount(); i++) {
//                    Chore tempChore = responsibility.getChore();
//                    dissassembleChore(tempChore);

//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        // check if admin, if not ,set invisible for add button and edit button
        Intent intent = getIntent();

        if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE) != null) {

            if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE).equals("user")) {
                Button add = findViewById(R.id.btnAdd);
                add.setVisibility(View.INVISIBLE);
            } else if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE).equals("admin")) {
                Button add = findViewById(R.id.btnAdd);
                add.setVisibility(View.VISIBLE);
            }
        } else {
            choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");
        }


        //get the PersonRule from Welcomepage
        testRule = (PersonRule) intent.getSerializableExtra("test");

        if (choreSubmit != null) {
            displayChore(choreSubmit);
        }

    }


    public void displaySampleChores(){
        long millis = cal.getTimeInMillis();

        for (int i = 0 ; i <5;i++){
            try {
                Chore tempChore = new Chore("Chore" + i, "Description of the chore", millis, i);
                displayChore(tempChore);
            } catch (NoSuchAlgorithmException ex) {
                System.out.println("ooops");
            }
        }

    }

    public void sumbitChore(Chore chore) {
        choreSubmit = chore;
    }

    public void add_OnClick(View view) {
        Intent intent = new Intent(ChoreList.this, ChoreEdit.class); //switch homepage to edit chore page
        startActivity(intent);
    }

    private void displayChore (Chore chore){

        //extract Calendar data from chore
        Calendar tempCal        = Calendar.getInstance();
        tempCal.setTimeInMillis( chore.getTimeInMillis() );

        int choreHours          = tempCal.get(Calendar.HOUR);
        int choreMinute         = tempCal.get(Calendar.MINUTE);
        int choreYear           = tempCal.get(Calendar.YEAR);
        int choreMonth          = tempCal.get(Calendar.MONTH);
        int choreDay            = tempCal.get(Calendar.DAY_OF_MONTH);

        //if chores day doesn't match the current displayed day exit
        if ((choreYear != year) || (choreMonth != month) || (choreDay != day)){
            return;
        }

        LinearLayout linearView = findViewById(R.id.choreView);

        GridLayout gridLayout   = new GridLayout(this);
        Point point             = new Point();                  //required for to get display size

        //create text views and add text to them
        TextView text1          = textTest(chore.getChoreName(),18);
        TextView text2          = textTest( String.format("%02d:%02d",choreHours, choreMinute), 14);
        TextView text3          = textTest(chore.getDescription(), 14);

        //set the layout
        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(2);
        text1.setGravity(Gravity.LEFT);
        text2.setGravity(Gravity.TOP | Gravity.LEFT);
        text3.setGravity(Gravity.LEFT);
        getWindowManager().getDefaultDisplay().getSize(point);
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

        //tag the chore for referencing
        gridLayout.setTag(chore);

        // Create a listener for touch screen events;
        gridLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Toast.makeText(getBaseContext(), " " + view.getTag(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoreList.this, showDetailDialog.class);
                startActivity(intent);

            }

        });

        //Create a listener for long touch screen events;
        gridLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getBaseContext(), "It is a long click event", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoreList.this, ChoreEdit.class);
                intent.putExtra("SUBMIT", (Chore) view.getTag());
                startActivity(intent);
                return true;

            }
        });

        linearView.addView(gridLayout);

    }

    /**
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

    public void textDate_OnClick(View view) {
        datePick();
    }

    public void imgDateUp_OnClick(View view) {
        buildNewDate(1);
    }

    public void imgDateDown_OnClick(View view) {
        buildNewDate(-1);
    }

    private void updateDate(int year, int month, int day){

        if ((year == this.year) && (month == this.month) && (day == this.day)) {
            return;
        }

        cal.set(year,month,day);

        this.year   = year;
        this.month  = month;
        this.day    = day;

        scrubChoreView();
    }

    private void buildNewDate(int upDown) {
        cal.add(Calendar.DATE, upDown);

        year    = cal.get(Calendar.YEAR);
        month   = cal.get(Calendar.MONTH);
        day     = cal.get(Calendar.DAY_OF_MONTH);

        updateDate(year, month, day);
        setDate(year, month, day);
    }

    private void scrubChoreView(){
        LinearLayout choreView = findViewById(R.id.choreView);
        choreView.removeAllViews();
    }

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

    private void datePick() {

        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
    }


}
