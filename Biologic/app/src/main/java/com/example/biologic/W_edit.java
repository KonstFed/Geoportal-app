package com.example.biologic;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class W_edit extends Widget {
    EditText editText;

    public W_edit(JSONObject field, Context context, LinearLayout.LayoutParams layoutParams) {
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
    public void addToLayout(LinearLayout layout) {
        LinearLayout lin = new LinearLayout(context);
        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setText(propertyName);
        lin.addView(textView);
        lin.addView(editText);
        layout.addView(lin);

    }

    @Override
    public View getView() {
        return editText;
    }
}
