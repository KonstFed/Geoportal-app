package com.example.biologic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingActvity extends AppCompatActivity {
    String cookie;
    HashMap<String,String> tables_map;
    Retrofit retrofit;
    String f;
    String old_f;
    String old_name;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode)
        {
            case 2:

        }
    }
    public void getTables(int iDisplayStart,int iDisplayLength)
    {
        GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
        Call<ResponseBody> call = geoportalConnect.getData(Integer.toString(100),iDisplayStart,iDisplayLength,"id,name");
        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                try
                {

                    String s = response.body().string();
                    JSONObject resp = new JSONObject(s);
                    tables_map = new HashMap<>();
                    JSONArray data_tables = resp.getJSONArray("aaData");
                    for (int i = 0; i <data_tables.length() ; i++) {

                        JSONObject tmp = data_tables.getJSONObject(i);
                        if (tmp.getString("id").equals(old_f))
                        {
                            old_name = tmp.getString("name");
                        }
                        tables_map.put(tmp.getString("name"),tmp.getString("id"));
                    }
                    final ArrayList<String> data= new ArrayList<>();
                    for (String si: tables_map.keySet()) {
                        data.add(si);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.support_simple_spinner_dropdown_item,data);

                    Spinner spinner = findViewById(R.id.spinner);
                    spinner.setAdapter(arrayAdapter);
                    spinner.setPrompt(old_name);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            f = tables_map.get(data.get(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            f= tables_map.get(data.get(0));
                        }
                    });

                    Log.e("e","dsada");
                }catch (Exception e){
                    Log.e("e",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("e","dsada");

            }
        };
        call.enqueue(callback);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_actvity);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);
        client.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Cookie", cookie).build();
                return chain.proceed(request);
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.urlstruct)) // адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        final SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cookie = myPreferences.getString("cookie","");
        Intent i = getIntent();
        old_f = i.getStringExtra("f");
        f = old_f;
        Button accept = findViewById(R.id.SettingAcceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("f",f);
                setResult(3,i);
                finish();
            }
        });
        Button cancel = findViewById(R.id.SettingCancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("f",old_f);
                setResult(3,i);
                finish();
            }
        });
        TextView text = findViewById(R.id.log);
        Button brexit = findViewById(R.id.brexit);
        text.setText(myPreferences.getString("login",""));
        brexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor myEditor = myPreferences.edit();
                myEditor.putString("login","");
                myEditor.putString("password","");
                myEditor.commit();
                Intent i = new Intent(getApplicationContext(),Registration.class);
                startActivityForResult(i,2);

            }
        });
        getTables(0,100);

    }
}
