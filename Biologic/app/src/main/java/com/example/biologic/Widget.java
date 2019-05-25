package com.example.biologic;

import android.content.Context;
import android.content.res.Resources;
import android.widget.LinearLayout;

import org.json.JSONObject;

public abstract class Widget  {
    public JSONObject field;
    public Context context;
    public LinearLayout.LayoutParams layoutParams;
    public String propertyName;

    public Widget(JSONObject field, Context context) {
        this.field = field;
        this.context = context;
        this.layoutParams = layoutParams;
        try{
            propertyName = field.getString("fieldname");
        }catch (Exception e){}



    }



//    public abstract void createWidget(JSONObject field);
    public abstract String getValue();
    public abstract void createView(LinearLayout linearLayout,String value);
    public abstract void control(LinearLayout layout,String data);
    public abstract void control(LinearLayout layout, String data, Resources res);
    public abstract void control(LinearLayout layout);
    public abstract void control(LinearLayout layout,Resources res);


}
