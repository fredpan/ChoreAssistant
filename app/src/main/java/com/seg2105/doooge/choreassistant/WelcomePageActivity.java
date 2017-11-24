package com.seg2105.doooge.choreassistant;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by fredpan on 2017/11/22.
 */

public class WelcomePageActivity extends ListActivity {

    // The Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[]{ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};
    // The select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";
    DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Login");
    LoginIconAdapter mAdapter; //The Adapter being used to display the list's data

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);
        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = findViewById(android.R.id.content);
        root.addView(progressBar);

        // For the cursor adapter, specify which columns go into which views
        //String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.button1}; // The TextView in simple_list_item_1

        ArrayList<String[]> buttonList = new ArrayList<>();
        String[] a = {"a", "b", "c"};
        buttonList.add(a);


        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new LoginIconAdapter(buttonList, this);
        setListAdapter(mAdapter);


    }


}
