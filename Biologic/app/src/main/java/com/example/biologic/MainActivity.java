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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private String url;
    private ImageView logo;
    private boolean isRegged;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = this.getResources();
        url = res.getString(R.string.urlstruct);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.testlenin);
        if (isOnline(this)) {
            String[] ur = new String[1];
            ur[0] = url;
            ServerConnect serverConnect = new ServerConnect();
            serverConnect.execute(ur);
            JSONObject tablestruct = serverConnect.getStructTable();
        }
        else
        {
            Toast.makeText(this,res.getString(R.string.noInternet),LENGTH_LONG).show();

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
