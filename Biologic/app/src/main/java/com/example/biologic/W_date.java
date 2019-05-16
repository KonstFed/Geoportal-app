package com.example.biologic;

import android.content.Context;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import org.json.JSONObject;

public class W_date extends Widget {
    String date = "";
    CalendarView calendarView;
    public W_date(JSONObject field, Context context, LinearLayout.LayoutParams layoutParams) {
        super(field, context, layoutParams);
        try {
            propertyName = field.getString("fieldname");
            calendarView = new CalendarView(context);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    date = year + "-" + month + "-" + dayOfMonth;
                }
            });

        } catch (Exception e) {
        }

    }

    @Override
    public String getValue() {
        return date;
    }

    @Override
    public View getView() {
        return calendarView;
    }


}
