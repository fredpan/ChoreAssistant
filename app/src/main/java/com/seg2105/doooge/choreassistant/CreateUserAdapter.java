package com.seg2105.doooge.choreassistant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

/**
 * Created by fredpan on 2017/11/24.
 */

public class CreateUserAdapter extends ArrayAdapter<Boolean> {
    List<Boolean> isAdmins;
    private Activity context;

    public CreateUserAdapter(Activity context, List<Boolean> isAdmins) {
        super(context, R.layout.registration, isAdmins);
        this.context = context;
        this.isAdmins = isAdmins;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View userListView = inflater.inflate(R.layout.registration, null, true);

        Button createUserButton = userListView.findViewById(R.id.createUser);
        createUserButton.setClickable(isAdmins.get(0));
        isAdmins.clear();
        return userListView;
    }
}
