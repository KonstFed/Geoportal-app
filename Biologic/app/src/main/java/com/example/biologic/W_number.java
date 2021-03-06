package com.example.biologic;

import android.content.Context;
import android.content.res.Resources;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class W_number extends Widget {
    EditText editText;

    public W_number(JSONObject field, Context context, Resources res) {
        super(field, context, res);
    }

    @Override
    public String getValue() {
        return editText.getText().toString();
    }
    public void control(LinearLayout layout,String data) {
        try {
            editText = new EditText(context);
            editText.setHint(field.getString("fieldname"));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setText(data);
            propertyName = field.getString("fieldname");

            LinearLayout lin = new LinearLayout(context);
            lin.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(context);
            textView.setText(titlename);
            textView.setTextSize(25);
            lin.addView(textView);
            lin.addView(editText);
            lin.setBackground(res.getDrawable(R.drawable.customborder));
            layout.addView(lin);
        }catch (Exception e){}
    }
    public void control(LinearLayout layout) {
        try {
            editText = new EditText(context);
            editText.setHint(field.getString("fieldname"));
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            propertyName = field.getString("fieldname");

            LinearLayout lin = new LinearLayout(context);
            lin.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(context);
            textView.setText(titlename);
            textView.setTextSize(25);
            lin.addView(textView);
            lin.addView(editText);
            lin.setBackground(res.getDrawable(R.drawable.customborder));
            layout.addView(lin);
        }catch (Exception e){}
    }
    @Override
    public void createView(LinearLayout linearLayout,String value) {
        LinearLayout lin = new LinearLayout(context);
        lin.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(context);
        textView.setText(titlename+": "+ value);
        textView.setTextSize(25);
        lin.addView(textView);
        linearLayout.addView(lin);
    }
}
