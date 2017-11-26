package com.seg2105.doooge.choreassistant;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by fredpan on 2017/11/22.
 */

public class WelcomePageActivity extends AppCompatActivity {
    public final static String EXTRA_MASSAGE = "Tag"; // the name of intent for this class



    // The Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[]{ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};
    // The select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";
    DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("Login");
    private RecyclerView mRecyclerView; //This view for display each user.
    private HomeAdapter mAdapter; //This adapter for control the recyclerView.
    private ArrayList<String> buttonList; //This list for save user.

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);


        buttonList = new ArrayList<>();
        buttonList.add("Vison1");
        buttonList.add("Dustin");
        buttonList.add("Fred");
        buttonList.add("Vison2");


        //Create a dapter to control the recyclerView.
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter = new HomeAdapter());

    }

    public void adminLogin(View view) {
        Intent intent = new Intent(WelcomePageActivity.this, ChoreList.class);
        String message = "admin";
        intent.putExtra(EXTRA_MASSAGE, message);
        startActivity(intent);

    }

    /*
    create listener for adminLogin button
     */

    /*
    ** To createa adapter control each user in the recyclerView.
    */
    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    WelcomePageActivity.this).inflate(R.layout.user_item, parent,
                    false));
            return holder;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.userButton.setText(buttonList.get(position));
            if (position == 0) {
                holder.userButton.setBackgroundResource(R.drawable.login_icon_green);
            } else if (position == 1) {
                holder.userButton.setBackgroundResource(R.drawable.login_icon_blue);
            } else if (position == 2) {
                holder.userButton.setBackgroundResource(R.drawable.login_icon_red);
            }

            setOnClickListener(holder.userButton, position);
        }
        /*
        ** To set onClickListener for each button.
        */

        public void setOnClickListener(Button button, final int position) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(WelcomePageActivity.this, ChoreList.class);
                    intent.putExtra(EXTRA_MASSAGE, "user");
                    startActivity(intent);
                }
            });
        }
        /*
        to let adapter record the size of user list
         */

        @Override
        public int getItemCount() {

            return buttonList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            Button userButton;

            public MyViewHolder(View view) {
                super(view);
                userButton = view.findViewById(R.id.col1);
            }
        }
    }


}
