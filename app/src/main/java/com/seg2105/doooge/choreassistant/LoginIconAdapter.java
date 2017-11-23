package com.seg2105.doooge.choreassistant;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * Created by fredpan on 2017/11/22.
 */

public class LoginIconAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;


    public LoginIconAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.three_user_per_line, null);
        }

//        //Handle TextView and display string from your list
//        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
//        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button button1 = view.findViewById(R.id.col1);
        Button button2 = view.findViewById(R.id.col2);
        Button button3 = view.findViewById(R.id.col2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                System.out.println("Click1");
                notifyDataSetChanged();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                System.out.println("Click1");
                notifyDataSetChanged();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                System.out.println("Click1");
                notifyDataSetChanged();
            }
        });

        return view;
    }

    public void swapCursor(Cursor data) {
    }
}