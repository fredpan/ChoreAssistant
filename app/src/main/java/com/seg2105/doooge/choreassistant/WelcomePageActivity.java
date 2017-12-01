package com.seg2105.doooge.choreassistant;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
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
    private final DatabaseReference databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
    private RecyclerView mRecyclerView; //This view for display each user.
    private HomeAdapter mAdapter; //This adapter for control the recyclerView.
    private PersonRule testRule;  // This is for test
    private ArrayList<PersonRule> personRulesList; //This is for save user
    private boolean noAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);


        //Create a dapter to control the recyclerView.


        //Getting users from db

        databaseLoginInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                personRulesList = new ArrayList<>();
                for (DataSnapshot personRoleInstance : dataSnapshot.getChildren()) {
                    PersonRule personRule = personRoleInstance.getValue(PersonRule.class);
                    personRulesList.add(personRule);

                }
                noAdmin = personRulesList.isEmpty();
                mRecyclerView = findViewById(R.id.id_recyclerview);
                mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mAdapter = new HomeAdapter());

                System.out.println("11111111=========================");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        System.out.println("2222===================");


        Button loginButton = findViewById(R.id.loginAsAdmin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noAdmin) {
                    Intent intent = new Intent(WelcomePageActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                } else {
                    showLoginDialog();
                }
            }
        });


    }


    //create a listener to display login Dialog for admin button
    public void showLoginDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Login :");


        final EditText userName = dialogView.findViewById(R.id.editID);
        final EditText password = dialogView.findViewById(R.id.editPassword);

        final Button login = dialogView.findViewById(R.id.LoginButton);
        final Button exist = dialogView.findViewById(R.id.ExistButton);
        final TextView warm = dialogView.findViewById(R.id.warm);
        warm.setVisibility(View.INVISIBLE);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (userName.getText().toString().equals("") || password.getText().toString().equals("")) {
                    warm.setText("Your userID or Password is wrong !!");
                    warm.setVisibility(View.VISIBLE);

                } else {
                    try {
                        isValidLogin(userName.getText().toString(), password.getText().toString(), dialog, warm);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void showPasswordConfirm(final PersonRule user) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.login_confirm, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Login :");


        final EditText password = dialogView.findViewById(R.id.editConfirmPassword);

        final Button login = dialogView.findViewById(R.id.confirm_button);
        final Button exist = dialogView.findViewById(R.id.confirm_exist);
        final TextView warm = dialogView.findViewById(R.id.confirm_warn);
        final AlertDialog dialog = dialogBuilder.create();
        dialog.show();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals("") || password.getText().toString().equals("")) {
                    warm.setText("Your Password is wrong !!");
                    warm.setVisibility(View.VISIBLE);

                } else {
                    //TODO Alg for verify admin pwd for going to main page
                    //isValidLogin(user.getUserName(),String.valueOf(password),dialog,warm);

//                    Intent intent = new Intent(WelcomePageActivity.this, ChoreList.class);
//                    intent.putExtra("currentUser", user);
//                    startActivity(intent);
//                    dialog.dismiss();
                }
            }
        });

        exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void isValidLogin(String userName, String password, final AlertDialog dialog, final TextView warm) throws NoSuchAlgorithmException {
        if (password == null) {
            password = "";
        }
        final String generatedIdentification = IdentificationUtility.generateIdentification(userName, password);
        databaseLoginInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot instanceOfUser : dataSnapshot.getChildren()) {
                    PersonRule personRule = instanceOfUser.getValue(PersonRule.class);

                    if (personRule.getEncrypted().equals(generatedIdentification)) {
                        Intent intent = new Intent(WelcomePageActivity.this, ControlPanelActivity.class);

                        startActivity(intent);
                        dialog.dismiss();
                    }
                }
                warm.setText("Your userID or Password is wrong !!");
                warm.setVisibility(View.VISIBLE);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

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
            PersonRule user = personRulesList.get(position);
            holder.userButton.setText(user.getUserName());
            holder.userButton.getBackground().setColorFilter(new LightingColorFilter(user.getColor(), user.getColor()));
            setOnClickListener(holder.userButton, position);
        }
        /*
        ** To set onClickListener for each button.
        */

        public void setOnClickListener(Button button, final int position) {
            final PersonRule user = personRulesList.get(position);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (user.isAdmin()) {
                        showPasswordConfirm(user);
                    } else {
                        Intent intent = new Intent(WelcomePageActivity.this, ChoreList.class);
                        intent.putExtra("currentUser", user);
                        startActivity(intent);
                    }
                }
            });
        }
        /*
        to let adapter record the size of user list
         */

        @Override
        public int getItemCount() {

            return personRulesList.size();
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
