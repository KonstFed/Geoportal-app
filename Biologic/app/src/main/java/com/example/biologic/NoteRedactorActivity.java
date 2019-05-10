package com.example.biologic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoteRedactorActivity extends AppCompatActivity {
    class Holder
    {
        View view;
    }
    JSONArray struct;
    private ArrayList<EditText> widgets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_redactor);
        Intent intent = getIntent();
        String[] datas = intent.getStringExtra("Note").split(":");


        try {
            struct = new JSONArray(datas[1]);
            for (int i = 0; i < struct.length(); i++) {
                EditText editText = new EditText(this);
                JSONObject tmp =struct.getJSONObject(i);
                editText.setText(tmp.getString("fieldname"));
                widgets.add(editText);
                Holder holder = new Holder();
                View view = LayoutInflater.from(this).inflate(R.layout.edit_field,(ViewGroup)findViewById( R.id.field),false);
                holder.view = editText;
                view.setTag(holder);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
       // struct = JSONObject(datas[1]);

    }
}
