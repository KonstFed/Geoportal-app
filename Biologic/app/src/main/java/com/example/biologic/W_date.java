package com.example.biologic;

import android.content.Context;
import android.content.res.Resources;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.jar.Attributes;

public class W_date extends Widget {
    String date = "";
    CalendarView calendarView;

    public W_date(JSONObject field, Context context, Resources res) {
        super(field, context, res);
    }

    @Override
    public String getValue() {
        return date;
    }

    @Override
    public void control(LinearLayout layout,String data) {
        try {

            propertyName = field.getString("fieldname");
            Attributes atr = new Attributes();
            calendarView = new CalendarView(context);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            String[] dataArray = data.split(" ")[0].split("-");
            calendar.set(Integer.parseInt( dataArray[0]),Integer.parseInt(dataArray[1])-1,Integer.parseInt(dataArray[2]));
            calendarView.setDate(calendar.getTimeInMillis());
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    date = year + "-" + month + "-" + dayOfMonth;
                }
            });

            LinearLayout layout1 = new LinearLayout(context);
            layout1.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(context);
            textView.setText(titlename);
            textView.setTextSize(25);
            layout1.addView(textView);
            layout1.addView(calendarView);
            layout1.setBackground(res.getDrawable(R.drawable.customborder));
            layout.addView(layout1);
        }catch (Exception e){}
    }
    @Override
    public void control(LinearLayout layout) {
        try {
//        View view = LayoutInflater.from(context).inflate(R.layout.date_maket,null);
//        layout.addView(view);
            propertyName = field.getString("fieldname");
            Attributes atr = new Attributes();
            calendarView = new CalendarView(context);
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                    date = year + "-" + month + "-" + dayOfMonth;
                }
            });

            LinearLayout layout1 = new LinearLayout(context);
            layout1.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(context);
            textView.setText(titlename);
            textView.setTextSize(25);
            layout1.addView(textView);
            layout1.addView(calendarView);
            layout1.setBackground(res.getDrawable(R.drawable.customborder));
            layout.addView(layout1);
        }catch (Exception e){}
    }
    @Override
    public void createView(LinearLayout linearLayout,String value) {

        LinearLayout layout1 = new LinearLayout(context);
        layout1.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(context);
        textView.setText(titlename+": "+value);
        textView.setTextSize(25);
        layout1.addView(textView);
        linearLayout.addView(layout1);
    }

}
