package com.seg2105.doooge.choreassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by dustin on 11/30/17.
 */


public class RewardEdit extends AppCompatActivity{

    private List<PersonRule> personRulesList;
    private List<PersonRule> selectedPersonRuleList;
    private List<Responsibility> selectedResponsibilityList;

    private Reward rewardSubmit;

    DatabaseReference databaseLoginInfo;
    DatabaseReference databaseReward;
    DatabaseReference databaseChore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_edit);

        Intent intent = getIntent();
        rewardSubmit = (Reward) intent.getSerializableExtra("reward");

        selectedResponsibilityList = new ArrayList<>();

        databaseReward      = FirebaseDatabase.getInstance().getReference("Reward");
        databaseLoginInfo   = FirebaseDatabase.getInstance().getReference("PersonRule");
        databaseChore       = FirebaseDatabase.getInstance().getReference("chore");

        userListen();               //listener for databaseLogInfo

    }

//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Dialog | Error handling - A message is displayed that warns the user of the occurence
     * of an error, once acknowledged by the user, it calles a function that will then return
     * the user to the Control Panel.
     *
     *
     * @param warningTitle
     * @param warningText
     */
    public void unhandledEvent(String warningTitle, String warningText){

        AlertDialog.Builder unhandled = new AlertDialog.Builder( this );
        unhandled.setTitle(warningTitle);
        unhandled.setMessage(warningText);
        unhandled.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controlPanelShow();
            }
        });

        unhandled.show();
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * User event - first, a new array is constructed containing the names of the all the
     * users who have been created - this array will be the list displayed to the user in
     * the form of a checkbox dialog. Another array is created to store the index of the
     * users that were clicked by the currentUser. When the user has finished making
     * their selection, the two lists are compared and text box is set to display to
     * the user the selections that were made. Then, a method is called and sent the array
     * containing the indexes of the selected users, a new array list is then constructed
     * which contains the users that were selected by the currentUser.
     *
     *
     * NOTE: the following site was referenced in the construction of this method
     * https://developer.android.com/reference/android/app/AlertDialog.Builder.html
     *
     */
    public void selectUsers(){

        //used in the creating of userList, list of all selected users
        final String[] users            = new String[personRulesList.size()];
        final ArrayList selectedUsers   = new ArrayList();

        for (int i = 0; i <personRulesList.size() ; i++){
            users[i] = personRulesList.get(i).getUserName();
        }

        AlertDialog.Builder userList = new AlertDialog.Builder(this);
        userList.setTitle("Select who can obtain the reward.");

        userList.setMultiChoiceItems(users, null, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked) {
                    selectedUsers.add(which);
                } else {
                    selectedUsers.remove(which);
                }
            }
        });


        userList.setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Creating a list of users to display in a textview
                StringBuilder sb    = new StringBuilder();

                for (int i = 0; i < selectedUsers.size();i++){
                    sb.append(users[ Integer.valueOf( selectedUsers.get(i).toString() ) ]);
                    if (i+1 < selectedUsers.size() ) { sb.append(", "); }
                }

                TextView textSelect = findViewById(R.id.textSelectUsers);
                textSelect.setText( sb.toString() );

                setSelectedPersonRuleList(selectedUsers);                   //call to set class variable
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


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * OnClick event - first, the method resets the textview and error messages, if any
     * had occured, then calls a method to enable the user to select users from a
     * selectable checkbox dialog.
     *
     *
     * @param view
     */
    public void textSelectUsers_OnClick(View view){
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtSelect = findViewById(R.id.textSelectUsers);
        txtSelect.setError(null);
        selectUsers();
    }


    /**
     * OnClick Event - User information as entered is verified and if valid, each selected
     * user is iterated and a new Reward is created and linked to the appropriate
     * responsibilities. The reward database is updated, then a method is called to
     * change intent back to the control panel.
     *
     *
     * @param view
     * @throws NoSuchAlgorithmException
     */
    public void btnSubmit_OnClick(View view) throws NoSuchAlgorithmException {

        TextView txtName = findViewById(R.id.textName);
        TextView txtDescription = findViewById(R.id.textDescription);
        TextView txtSelectedUsers = findViewById(R.id.textSelectUsers);

        Boolean allPass = true;

        if (txtName.getText().toString().trim().equals("")) {
            allPass = false;
            txtName.setError("Enter a name.");
            txtName.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if ((selectedPersonRuleList != null) && (selectedPersonRuleList.size() == 0)) {
            allPass = false;
            txtSelectedUsers.setError("Please assign a user.");
            txtSelectedUsers.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
        if(allPass){
            String name = txtName.getText().toString().trim();
            String description  = txtDescription.getText().toString().trim();

            if( selectedResponsibilityList == null || selectedPersonRuleList == null ){
                unhandledEvent( "Error!", "Creation of reward could not finish." );
                return;
            }

            //Create a Reward and associate it to a Responsibility
            for ( PersonRule person : selectedPersonRuleList ){
                int id = person.getUserID();
                Reward reward = new Reward(person);
                reward.setRewardName(name);
                reward.setRewardDescription(description);

                //check if the responsibility was assigned to this user, if so add it
                //to rewards responsibility list
                for(Responsibility responsibility : selectedResponsibilityList){
                    if ( person.getUserID() == responsibility.getUserID() ){
                        reward.addResponsibility(responsibility);
                    }
                }

                databaseReward.child( reward.getUserName() ).setValue(reward);

            }

            controlPanelShow();
        }

    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * UI Event - removes all currently displayed responsibilities from respoonsibilityView
     *
     *
     */
    private void scrubResponsibilityView(){
        LinearLayout responsibilityView = findViewById(R.id.responsibilityView);
        responsibilityView.removeAllViews();
    }


    /**
     * UI Event - iterates through all the users that were selected by the current user,
     * meanwhile it is also created check boxes and using a string builder to collect the
     * name of the user and identify which responsibility they are attached to. The
     * text collected by the string builder is then displayed as a check box, the
     * responsibility is then tagged and a listener is created, when activated the listener
     * calls a method to add the responsibility to the reward list.
     *
     *
     */
    private void setViewUp(){
        LinearLayout responsibilityView = findViewById(R.id.responsibilityView);

        for(PersonRule person : selectedPersonRuleList){
            for(Responsibility responsibility : person.getResponsibilities()){

                CheckBox checkBox = new CheckBox( getApplicationContext() );
                checkBox.setTextSize(18);

                StringBuilder sb = new StringBuilder();
                sb.append(person.getUserName() + ": ");
                sb.append( databaseChore.child(responsibility
                        .getChoreIdentification()).child("choreName").getKey() );

                checkBox.setText(sb.toString());
                checkBox.setTag( responsibility );

                final Responsibility res = responsibility;

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        addToSelectedRewardList(isChecked, res);                //set class variable
                    }
                });

                responsibilityView.addView(checkBox);
            }
        }
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Set Class Variables - depending on the data sent, the method will either add or
     * remove a responsibility from the selected responsibilities list.
     *
     *
     * @param isChecked indicates whether or not a check box has been selected
     * @param responsibility if the boolean is true, then add the responsibility to selected list
     */
    private void addToSelectedRewardList(boolean isChecked, Responsibility responsibility){
        if(isChecked){
            selectedResponsibilityList.add(responsibility);
        }else {
            selectedResponsibilityList.remove(responsibility);
        }

    }


    /**
     * Set Class Variables - Recieves an array of integers that correspond to
     * the personRuleList, a list of selected users is then created and a method is called
     * to clear the UI and, once completed, another method is called to redraw the layout
     * with checkboxes obtaining related responsibilities to the selected users.
     *
     *
     * @param selectedUserAtIndex
     */
    public void setSelectedPersonRuleList(ArrayList selectedUserAtIndex){

        selectedPersonRuleList= new ArrayList<>();

        for (int i = 0 ; i < selectedUserAtIndex.size() ; i++ ){
            selectedPersonRuleList
                    .add(personRulesList
                            .get( Integer.valueOf(selectedUserAtIndex.get(i).toString() )
                            )
                    );
        }

        scrubResponsibilityView();
        setViewUp();
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Listener - Fills the personRulesList with all the users in firebase.
     * If a reward was passed through intent, a new list is made and filled
     * with the users that were assigned to that reward. The list of users is
     * displayed to the currentUser in a TextView
     *
     *
     */
    public void userListen() {
        personRulesList = new ArrayList<>();

        databaseLoginInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear and rebuild personRule list
                personRulesList = new ArrayList<>();
                for (DataSnapshot personRoleInstance : dataSnapshot.getChildren()) {
                    personRulesList.add(personRoleInstance.getValue(PersonRule.class));
                }
                if (rewardSubmit == null) return;

                //fill selectedPersonList if a reward was passed through intent, and display.
                StringBuilder userID = new StringBuilder();
                List<Responsibility> responsibilities = rewardSubmit.getResponsibilities();
                for (int i = 0; i < responsibilities.size(); i++) {
                    int tempID = responsibilities.get(i).getUserID();

                    for (PersonRule user : personRulesList) {
                        if (tempID == user.getUserID()) {
                            userID.append(user.getUserName());
                            selectedPersonRuleList.add(user);
                        }
                    }
                    if (i + 1 < responsibilities.size()) { userID.append(", "); }
                }
                TextView textSelectedUsers = findViewById(R.id.textSelectUsers);
                textSelectedUsers.setText(userID.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Intent - Switches the intent back to the control panel
     *
     *
     */
    private void controlPanelShow(){
        Intent intent = new Intent(RewardEdit.this, ControlPanelActivity.class);
        startActivity(intent);
    }

}



