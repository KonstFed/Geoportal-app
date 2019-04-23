package com.example.biologic;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ConnectServ extends AsyncTask<String,Integer,String> {
    String command,URLserv;
    MainActivity mainform;
    public ConnectServ(String command,MainActivity mainform) {
        this.command = command;
        this.mainform = mainform;
    }

    private String getStruct(String URLserv)
    {
        HttpURLConnection urlConnection = null;
        String serviceUrl = URLserv;
        try {
            URL url = new URL(serviceUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setReadTimeout(5000);
            urlConnection.connect();


            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String response = "";
            String line;
            while ((line = rd.readLine()) != null) {
                response = response + line;
            }
            rd.close();
            return response;
        }
        catch (ProtocolException e) {
            Log.e("request","ProtocolException error");
        } catch (MalformedURLException e) {
            Log.e("request","MalformedURLException error");

        } catch (IOException e) {
            Log.e("request","IOException error");
        }
        if (urlConnection != null){
            urlConnection.disconnect();
        }
        return null;
    }
    @Override
    protected String doInBackground(String... strings) {
        switch (command)
        {
            case "structure":
                return getStruct(strings[0]);

            default:
                return "";
        }
    }

    @Override
    protected void onPostExecute(String s) {
        switch (command)
        {
            case "structure":
                try {
                    JSONObject struct = new JSONObject(s);
                    JSONArray aadata = struct.getJSONArray("aaData");
                    if (aadata.length()==1)
                    {
                        JSONObject tmp = aadata.getJSONObject(0);
                        String tabledesc = tmp.getString("JSON");
                        JSONObject struct2 = new JSONObject(tabledesc);
                        mainform.drawform(struct2);
                    }


                } catch (JSONException e) {
                    Log.d("serverResponce", "struct dnt parse");

                }
        }
    }
}
