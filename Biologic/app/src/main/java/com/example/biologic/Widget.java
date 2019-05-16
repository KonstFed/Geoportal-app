package com.example.biologic;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONObject;

public abstract class Widget  {
    public JSONObject field;
    public Context context;
    public LinearLayout.LayoutParams layoutParams;
    public String propertyName;
    public Widget(JSONObject field, Context context, LinearLayout.LayoutParams layoutParams) {
        this.field = field;
        this.context = context;
        this.layoutParams = layoutParams;
    }



//    public abstract void createWidget(JSONObject field);
    public abstract String getValue();
    public abstract View getView();

}
