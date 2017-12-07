package com.seg2105.doooge.choreassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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


public class RewardEdit extends AppCompatActivity {

    DatabaseReference databaseLoginInfo;
    DatabaseReference databaseReward;
    DatabaseReference databaseChore;
    private List<PersonRule> personRulesList;
    private List<PersonRule> selectedPersonRuleList;
    private List<Reward> rewardsList;
    private Reward rewardSubmit;
    private PersonRule currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_edit);

        Intent intent = getIntent();
        currentUser = (PersonRule) intent.getSerializableExtra("currentUser");

        rewardSubmit = null;

        databaseReward = FirebaseDatabase.getInstance().getReference("Reward");
        databaseLoginInfo = FirebaseDatabase.getInstance().getReference("PersonRule");
        databaseChore = FirebaseDatabase.getInstance().getReference("chore");

        userListen();               //listener for databaseLogInfo
        rewardListen();


    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Dialog | Error handling - A message is displayed that warns the user of the occurence
     * of an error, once acknowledged by the user, it calles a function that will then return
     * the user to the Control Panel.
     *
     * @param warningTitle
     * @param warningText
     */
    public void unhandledEvent(String warningTitle, String warningText) {

        AlertDialog.Builder unhandled = new AlertDialog.Builder(this);
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
     * <p>
     * <p>
     * NOTE: the following site was referenced in the construction of this method
     * https://developer.android.com/reference/android/app/AlertDialog.Builder.html
     */
    public void selectUsers() {

        //used in the creating of userList, list of all selected users
        final String[] users = new String[personRulesList.size()];
        final ArrayList selectedUsers = new ArrayList<>();

        for (int i = 0; i < personRulesList.size(); i++) {
            users[i] = personRulesList.get(i).getUserName();
        }

        AlertDialog.Builder userList = new AlertDialog.Builder(this);
        userList.setTitle("Select who can obtain the reward.");

        userList.setMultiChoiceItems(users, null, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    selectedUsers.add(which);
                } else {
                    selectedUsers.remove(Integer.valueOf(which));
                }
            }
        });

        userList.setPositiveButton("Sumbit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Creating a list of users to display in a textview
                StringBuilder sb = new StringBuilder();

                for (int i = 0; i < selectedUsers.size(); i++) {
                    sb.append(users[Integer.valueOf(selectedUsers.get(i).toString())]);
                    if (i + 1 < selectedUsers.size()) {
                        sb.append(", ");
                    }
                }

                TextView textSelect = findViewById(R.id.textSelectUsers);
                textSelect.setText(sb.toString());

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
     * @param view
     */
    public void textSelectUsers_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtSelect = findViewById(R.id.textSelectUsers);
        txtSelect.setError(null);
        selectUsers();
        toggleDeleteButton(false);
    }

    /**
     * OnClick event - Confirmation and deletion of a reward from the database.
     * A dialog message prompts the user for confirmation, then if confirmed,
     * carries out the removal of the reward from the database.
     *
     * @param view
     */
    public void btnDelete_OnClick(View view) {
        AlertDialog.Builder deleteConfirm = new AlertDialog.Builder(this);
        deleteConfirm.setTitle("Delete");
        deleteConfirm.setMessage("Are you sure you want to delete?");
        deleteConfirm.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                databaseReward.child(rewardSubmit.getRewardName()).removeValue();
                scrubRewardView();

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
     * OnClick event - first, the method resets the textview and error messages, if any
     * had occured.
     *
     * @param view
     */
    public void textName_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtName = findViewById(R.id.textName);
        txtName.setError(null);
        toggleDeleteButton(false);
    }


    /**
     * OnClick Event
     * Resets the text field back to its original state and disables the delete button
     * since it is assumed that the user would rather edit then delete.
     *
     * @param view
     */
    public void textDescription_OnClick(View view) {
        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
        TextView txtPoints = findViewById(R.id.textDescription);
        txtPoints.setError(null);
        toggleDeleteButton(false);
    }


    /**
     * OnClick Event - User information as entered is verified and if valid, each selected
     * user is iterated and a new Reward is created and linked to the appropriate
     * responsibilities. The reward database is updated, then a method is called to
     * change intent back to the control panel.
     *
     * @param view
     * @throws NoSuchAlgorithmException
     */
    public void btnSubmit_OnClick(View view) throws NoSuchAlgorithmException {

        //scrubRewardView();
        TextView txtName = findViewById(R.id.textName);
        TextView txtDescription = findViewById(R.id.textDescription);
        TextView txtSelectedUsers = findViewById(R.id.textSelectUsers);

        Boolean allPass = true;

        try {
            Integer.parseInt(txtDescription.getText().toString().trim());
        } catch (NumberFormatException exNum) {
            allPass = false;
            txtDescription.requestFocus();
            txtDescription.setError("Enter a number.");
            txtDescription.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }

        if (txtName.getText().toString().trim().equals("")) {
            allPass = false;
            txtName.setError("Enter a name.");
            txtName.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
        }
//        if ((selectedPersonRuleList == null) || (selectedPersonRuleList.size() == 0) ||
//                txtSelectedUsers.getText().toString().trim().equals("") ) {
//            allPass = false;
//            txtSelectedUsers.setError("Please assign a user.");
//            txtSelectedUsers.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_red));
//        }
        if (allPass) {
            String name = txtName.getText().toString().trim();

//            if( selectedPersonRuleList == null ){
//                unhandledEvent( "Error!", "Creation of reward could not finish." );
//                return;
//            }

            if (rewardSubmit != null) {
                databaseReward.child(rewardSubmit.getRewardName()).removeValue();
            }

            Reward reward = new Reward();
            reward.setRewardName(name);
            reward.setPoints(Integer.parseInt(txtDescription.getText().toString().trim()));

            if (selectedPersonRuleList != null) {
                for (PersonRule person : selectedPersonRuleList) {
                    Responsibility responsibility = new Responsibility(person.getUserID(), name);
                    reward.addResponsibility(responsibility);
                }
            }

            databaseReward.child(name).setValue(reward);

            controlPanelShow();
        }

    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * UI Event - removes all currently displayed responsibilities from respoonsibilityView
     */
    private void scrubRewardView() {
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
     */
    private void displayReward(Reward reward) {
        LinearLayout responsibilityView = findViewById(R.id.responsibilityView);

        LinearLayout top = new LinearLayout(this);

        top.setOrientation(LinearLayout.VERTICAL);

        TextView text1 = new TextView(this);
        TextView text2 = new TextView(this);

        text1.setTextSize(18);
        text2.setTextSize(14);

        text1.setTypeface(Typeface.DEFAULT_BOLD);
        text2.setTypeface(null, Typeface.BOLD_ITALIC);

        text1.setText(reward.getRewardName());
        text2.setText(reward.getPoints() + " Points - " + setSelectedPersonRuleList(reward));

        top.addView(text1);
        top.addView(text2);

        top.setPadding(0, 30, 0, 30);

        top.setTag(reward);

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rewardSubmit = ((Reward) view.getTag());

                TextView txtName = findViewById(R.id.textName);
                TextView txtPoints = findViewById(R.id.textDescription);
                TextView txtUsers = findViewById(R.id.textSelectUsers);

                txtName.setText(rewardSubmit.getRewardName());
                txtPoints.setText(String.valueOf(rewardSubmit.getPoints()));
                txtUsers.setText(setSelectedPersonRuleList(rewardSubmit));

                txtName.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
                txtPoints.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));
                txtUsers.setBackgroundDrawable(getResources().getDrawable(R.drawable.back));

                txtName.setError(null);
                txtPoints.setError(null);
                txtUsers.setError(null);

                toggleDeleteButton(true);
            }
        });

        responsibilityView.addView(top);
    }

    private String setSelectedPersonRuleList(Reward reward) {
        selectedPersonRuleList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Responsibility responsibility : reward.getResponsibilities()) {
            for (PersonRule person : personRulesList) {
                if (responsibility.getUserID() == person.getUserID()) {
                    selectedPersonRuleList.add(person);
                    sb.append(person.getUserName());

                    if (responsibility.isComplete()) {
                        sb.append(": completed");
                    }
                }
            }

            if (i + 1 < reward.getResponsibilities().size()) {
                sb.append(";    ");
            }
            i++;
        }
        return sb.toString();
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Set Class Variables - Recieves an array of integers that correspond to
     * the personRuleList, a list of selected users is then created and a method is called
     * to clear the UI and, once completed, another method is called to redraw the layout
     * with checkboxes obtaining related responsibilities to the selected users.
     *
     * @param selectedUserAtIndex
     */
    public void setSelectedPersonRuleList(ArrayList selectedUserAtIndex) {

        selectedPersonRuleList = new ArrayList<>();

        for (int i = 0; i < selectedUserAtIndex.size(); i++) {
            selectedPersonRuleList
                    .add(personRulesList
                            .get(Integer.valueOf(selectedUserAtIndex.get(i).toString())
                            )
                    );
        }
    }


//--------------------------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------


    /**
     * Listener - Fills the personRulesList with all the users in firebase.
     * If a reward was passed through intent, a new list is made and filled
     * with the users that were assigned to that reward. The list of users is
     * displayed to the currentUser in a TextView
     */
    public void userListen() {
        personRulesList = new ArrayList<>();

        databaseLoginInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clear and rebuild personRule list
                personRulesList = new ArrayList<>();
                for (DataSnapshot personRoleInstance : dataSnapshot.getChildren()) {
                    PersonRule user = personRoleInstance.getValue(PersonRule.class);
                    if (!user.isAdmin()) {
                        personRulesList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    /**
     * Listener - Fills rewardsList with all the avaiable awards and then calls
     * a method to have them displayed on the UI.
     */
    private void rewardListen() {
        scrubRewardView();
        rewardsList = new ArrayList<>();

        databaseReward.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    Reward reward = snapShot.getValue(Reward.class);
                    rewardsList.add(reward);
                    displayReward(reward);
                }

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
     */
    private void controlPanelShow() {
        Intent intent = new Intent(RewardEdit.this, ControlPanelActivity.class);
        intent.putExtra("currentUser", currentUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    /**
     * Disables or enables the "Delete" button
     *
     * @param flag
     */
    private void toggleDeleteButton(boolean flag) {
        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setEnabled(flag);
    }

}



