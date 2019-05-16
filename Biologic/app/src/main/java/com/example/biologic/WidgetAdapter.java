package com.example.biologic;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class WidgetAdapter extends BaseAdapter {
    ArrayList<Widget> widgets;
    Context context;

    public WidgetAdapter(ArrayList<Widget> widgets, Context context) {
        this.widgets = widgets;
        this.context = context;
    }

    @Override
    public int getCount() {
        return widgets.size();
    }

    @Override
    public Object getItem(int position) {
        return widgets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Widget curwidg = widgets.get(position);
        convertView = curwidg.getView();
        return convertView;
    }
}
