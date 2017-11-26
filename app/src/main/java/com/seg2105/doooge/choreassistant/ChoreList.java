package com.seg2105.doooge.choreassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;


/**
 * Created by dustin on 11/22/17.
 */

public class ChoreList extends AppCompatActivity {

    private static Chore choreSubmit;
    private final String CHORE_RED = "#ffff4444";
    private final String CHORE_PURPLE = "#ffaa66cc";
    private final String CHORE_ORANGE = "#ffff8800";
    private final String CHORE_GREEN = "#ff99cc00";
    private final String CHORE_BLUE = "#ff0099cc";
    DatabaseReference databaseChore = FirebaseDatabase.getInstance().getReference("Chore");
    private int day;
    private int month;
    private int year;
    private Calendar cal;
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

        final LinearLayout linearView = findViewById(R.id.choreView);

        /*
        Drawable[] draw = {
                getResources().getDrawable(R.drawable.back),
        };
        */


        //SAMPLE CHOREs
        try {
            Chore chore1 = new Chore("Chore 1", "THis is a fake chore", cal);
            Chore chore2 = new Chore("Chore 2", "THis is a fake chore", cal);
            Chore chore3 = new Chore("Chore 3", "THis is a fake chore", cal);
            databaseChore.child(chore1.getChoreIdentification()).setValue(chore1);
            databaseChore.child(chore2.getChoreIdentification()).setValue(chore2);
            databaseChore.child(chore3.getChoreIdentification()).setValue(chore3);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        databaseChore.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (long i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    GridLayout temp = layoutTest("Chore" + i, "TIME", "Name"); //, draw[i%5]);

                    temp.setTag("chore" + i);
                    linearView.addView(temp);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // check if admin, if not ,set invisible for add button and edit button
        Intent intent = getIntent();

        if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE) != null) {

            if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE).equals("user")) {
                Button add = (Button) findViewById(R.id.btnAdd);
                Button edit = (Button) findViewById(R.id.btnEdit);
                add.setVisibility(View.INVISIBLE);
                edit.setVisibility(View.INVISIBLE);
            } else if (intent.getStringExtra(WelcomePageActivity.EXTRA_MASSAGE).equals("admin")) {
                Button add = (Button) findViewById(R.id.btnAdd);
                Button edit = (Button) findViewById(R.id.btnEdit);
                add.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }
        } else {
            choreSubmit = (Chore) intent.getSerializableExtra("SUBMIT");
        }


    }

    public void sumbitChore(Chore chore) {
        choreSubmit = chore;

    }



    public void add_OnClick(View view) {
//        startActivity(new Intent(this,ChoreEdit.class));

    }

    /**
     *
     * @param name name of chore to be created
     * @param time time that chore is to be completed
     * @param description description of chore
     * @return returns a grid layout with chore added into it
     */
    private GridLayout layoutTest(String name, String time, String description){ //, Drawable colorTemp){

        //Create grid layout and textviews
        GridLayout tempLayout   = new GridLayout(this);
        Point point             = new Point();                  //required for to get display size
        TextView text1          = textTest(name,30);
        TextView text2          = textTest(time, 24);
        TextView text3          = textTest(description, 24);

        //set grid layout size
        tempLayout.setColumnCount(2);
        tempLayout.setRowCount(2);

        //set textview position
        text1.setGravity(Gravity.LEFT);
        text2.setGravity(Gravity.TOP); // | Gravity.RIGHT);
        text3.setGravity(Gravity.LEFT);

        //text1.setTextColor(Color.parseColor(colorTemp));

        //get display size and use it to set textbox size.
        getWindowManager().getDefaultDisplay().getSize(point);
        text1.setWidth(point.x - 100);
        text2.setWidth( 100 );
        text3.setWidth(point.x - 100);

        text1.setTypeface(Typeface.DEFAULT_BOLD);
        text3.setTypeface(Typeface.DEFAULT_BOLD);

        //add the created textview to the grid layout
        tempLayout.addView(text1);
        tempLayout.addView(text2);
        tempLayout.addView(text3);


        //tempLayout.setBackgroundDrawable(colorTemp);
        //tempLayout.setBackgroundColor(Color.parseColor(colorTemp));


        tempLayout.setTag(name);

        // Create a listener for touch screen events;
        tempLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(getBaseContext(), " " + view.getTag(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoreList.this, showDetailDialog.class);
                startActivity(intent);
            }

        } );

        //Create a listener for long touch screen events;
        tempLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getBaseContext(), "It is a long click event", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChoreList.this, showDetailDialog.class);
                startActivity(intent);
                return true;

            }
        });




        return tempLayout;
    }



    /**
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

    public void textDate_OnClick(View view) {
        datePick();
    }

    public void imgDateUp_OnClick(View view){
        buildNewDate(1);
    }

    public void imgDateDown_OnClick(View view){
        buildNewDate(-1);
    }

    private void updateDate(int year, int month, int day){
        cal.set(year,month,day);
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private void buildNewDate(int upDown){
        cal.add(Calendar.DATE, upDown);
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        setDate(year,month,day);
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
                "December" };

        textDate.setText(monthString[month] + " " + day + ", " + year);
    }

    private void datePick() {

        DatePickerDialog temp = new DatePickerDialog(this, tempListen, year, month, day);
        temp.show();
    }

    //add on click listener for editing chore button
    public void editChore(View view) {
        Intent intent = new Intent(ChoreList.this, ChoreEdit.class); //switch homepage to edit chore page
        startActivity(intent);
    }





}
