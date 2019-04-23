package com.example.biologic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private String url;
    private ImageView logo;
    private boolean isRegged;
    RelativeLayout mainLayer;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = this.getResources();
        url = res.getString(R.string.urlstruct);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.testlogoussr);


        if (isOnline(this)) {
            String[] ur = new String[1];
            ur[0] = url;
            ConnectServ serverConnect = new ConnectServ("structure",this);
            serverConnect.execute(ur);
        }
        else
        {
            Toast.makeText(this,res.getString(R.string.noInternet),LENGTH_LONG).show();

        }
    }
    public void drawform(JSONObject struct)
    {
        mainLayer = (RelativeLayout) findViewById(R.id.mainactivity);
//        LayoutInflater inflater = getLayoutInflater();

        try {
            JSONArray columns = struct.getJSONArray("columns");
            TextView f = (TextView) findViewById(R.id.ssa);
            for (int i = 0; i < columns.length(); i++) {
                JSONObject column = columns.getJSONObject(i);
                f.setText(f.getText()+" "+ column.getString("fieldname") + "///////////////////");
                if( column.has("widget")) {
                    JSONObject widget = column.getJSONObject("widget");
                    String widgetname = widget.getString("name");

                    switch (widgetname)
                    {
                        case "edit":
//                            EditText field = inflater.inflate(R.layout.fieldedit);
                            EditText field = new EditText(this);

                            RelativeLayout.LayoutParams editviewparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            field.setLayoutParams(editviewparams);
                            mainLayer.addView(field);
                            //Создаем поле ввода (нормальное)
                            break;
                        case "data":
                            //типо календарь
                            break;
                        case "number":
                            // число
                            break;
                    }
                }
            }


        }
        catch (JSONException e) {
        e.printStackTrace();
    }
    }
    public void onMyButtonClick(View view)
    {
    }


    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
