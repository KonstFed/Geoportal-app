package com.example.biologic;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import org.json.JSONArray;

import java.util.ArrayList;

public class OnClickListennerRedactActivity implements View.OnClickListener {
    String id;
    ArrayList<String> data;
    JSONArray columns;
    Context context;
    public OnClickListennerRedactActivity(String id, ArrayList<String> data, JSONArray columns, Context context) {
        this.id = id;
        this.data = data;
        this.columns = columns;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(context,NoteRedactorActivity.class);
        i.putExtra("data",data);
        i.putExtra("id",id);
        i.putExtra("columns",columns.toString());
        context.startActivity(i);
    }
}
