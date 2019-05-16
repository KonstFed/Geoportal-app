package com.example.biologic;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONObject;

public class W_number extends Widget {
    EditText editText;

    public W_number(JSONObject field, Context context, LinearLayout.LayoutParams layoutParams) {
        super(field,context, layoutParams);
        try {
            editText = new EditText(context);
            editText.setHint(field.getString("fieldname"));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            propertyName = field.getString("fieldname");
//            linearLayout.addView(editText, layoutParams);
        }
        catch (Exception e)
        {

        }
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
