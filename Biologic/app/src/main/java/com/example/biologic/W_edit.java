package com.example.biologic;

import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class W_edit extends Widget {
    EditText editText;


    public W_edit(JSONObject field, Context context, Resources res) {
        super(field, context, res);
    }

    @Override
    public String getValue() {
        return editText.getText().toString();
    }

    public void control(LinearLayout layout, String data) {
        try {
            editText = new EditText(context);
//            editText.setHint(field.getString("fieldname"));
            editText.setText(data);
            LinearLayout lin = new LinearLayout(context);
            lin.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(context);
            textView.setTextSize(25);
            textView.setText(titlename);
            lin.addView(textView);
            lin.addView(editText);
            layout.addView(lin);
        }catch (Exception e){}
    }

    public void control(LinearLayout layout) {
        try {
            editText = new EditText(context);
            editText.setHint(field.getString("fieldname"));
            LinearLayout lin = new LinearLayout(context);
            lin.setOrientation(LinearLayout.VERTICAL);
            TextView textView = new TextView(context);
            textView.setTextSize(25);
            textView.setText(titlename);
            lin.addView(textView);
            lin.addView(editText);
            lin.setBackground(res.getDrawable(R.drawable.customborder));
            layout.addView(lin);
        }catch (JSONException e){}
    }
    @Override
    public void createView(LinearLayout lin,String value) {
        TextView textView = new TextView(context);
        textView.setText(titlename+ ": "+ value);
        textView.setTextSize(30);
        lin.addView(textView);

    }
}
