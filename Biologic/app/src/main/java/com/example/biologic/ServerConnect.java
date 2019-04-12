package com.example.biologic;

import android.os.AsyncTask;
import android.util.Log;
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


public class ServerConnect extends AsyncTask<String,Integer,String> {
    JSONObject structTable = null;
    @Override
    protected String doInBackground(String... urls) {


        HttpURLConnection urlConnection = null;
        String serviceUrl = urls[0];
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
    protected void onPostExecute(String s) {
        try {
            structTable = new JSONObject(s);
        } catch (JSONException e) {
            Log.e("Json","Help");
        }

        super.onPostExecute(s);

    }
    public JSONObject getStructTable()
    {
        return structTable;
    }

}
