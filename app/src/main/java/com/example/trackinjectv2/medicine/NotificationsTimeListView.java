package com.example.trackinjectv2.medicine;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trackinjectv2.R;

import java.util.Calendar;
import java.util.List;

public class NotificationsTimeListView extends ArrayAdapter<Time> {
    private List<Time> list;
    private Activity context;


    public NotificationsTimeListView(Activity context, List<Time> list) {
        super(context, R.layout.layout_time_list, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_time_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.pickedTime);
        int hour = list.get(position).getHour();
        int minute = list.get(position).getMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(0,0,0,hour,minute);
        textViewName.setText(DateFormat.format("hh:mm aa", calendar));
        return listViewItem;
    }
}
