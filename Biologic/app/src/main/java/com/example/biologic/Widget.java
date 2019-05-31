package com.example.biologic;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.widget.LinearLayout;

import org.json.JSONObject;

public abstract class Widget extends Activity {
    public JSONObject field;
    public Context context;
    public LinearLayout.LayoutParams layoutParams;
    public String propertyName;
    public String titlename;
    public Resources res;
    public Widget(JSONObject field, Context context,Resources res) {
        this.field = field;
        this.context = context;
        this.res = res;
        try{
            propertyName = field.getString("fieldname");
            titlename = field.getString("title");
        }catch (Exception e){}



    }



//    public abstract void createWidget(JSONObject field);
    public abstract String getValue();
    public abstract void createView(LinearLayout linearLayout,String value);
    public abstract void control(LinearLayout layout, String data);
    public abstract void control(LinearLayout layout);


}
