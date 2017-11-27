package com.seg2105.doooge.choreassistant;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fredpan on 2017/11/24.
 */

public class UserInfoAdapter extends ArrayAdapter<PersonRule> {
    private ArrayList<PersonRule> listOfUser;
    private Activity context;


    public UserInfoAdapter(ArrayList<PersonRule> list, Activity context) {
        super(context, R.layout.single_user_info, list);
        this.listOfUser = list;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.single_user_info, null);


//        //Handle TextView and display string from your list
//        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
//        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        final PersonRule user = listOfUser.get(position);
        TextView displayName = view.findViewById(R.id.displayName);
        TextView userAuth = view.findViewById(R.id.userAuth);
        displayName.setText(user.getUserName());
        userAuth.setText("");
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, EditUserActivity.class);
                intent.putExtra("u", user);
                context.startActivity(intent);
            }
        });

        view.setBackgroundColor(user.getColor());


        return view;
    }
}
