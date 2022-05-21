package com.example.trackinjectv2.Injections;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trackinjectv2.R;

import java.util.List;

public class InjectionsListView extends ArrayAdapter<Injections> {
    private Activity context;
    private List<Injections> list;

    public InjectionsListView(Activity context, List<Injections> list) {
        super(context, R.layout.layout_medicine_list, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_injections_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewNameAndLocation);
        Injections injection = list.get(position);
        textViewName.setText((injection.getLocationName()).concat("  ").concat(String.valueOf(injection.getInjectionLocationNumber())));
        return listViewItem;
    }
}
