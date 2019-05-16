package com.example.biologic;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

public class W_edit extends Widget {
    EditText editText;

    public W_edit( JSONObject field,Context context, LinearLayout.LayoutParams layoutParams) {
        super(field,context, layoutParams);
        try {
            editText = new EditText(context);
            editText.setHint(field.getString("fieldname"));
            propertyName = field.getString("fieldname");
        }
        catch (Exception e){}
    }



    @Override
    public String getValue() {
        return editText.getText().toString();
    }

    @Override
    public View getView() {
        return editText;
    }
}
