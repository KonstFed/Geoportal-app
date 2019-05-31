package com.example.biologic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration extends AppCompatActivity {
    Retrofit retrofit;
    String url;
    EditText username;
    EditText password;
    String cookie_data = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        url = getResources().getString(R.string.urlstruct);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(url) // адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
//                .client(client.build())
                .build();
        Button confirmButtom = findViewById(R.id.confirm);
        confirmButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
                Call<ResponseBody> call = geoportalConnect.regIn(username.getText().toString(),password.getText().toString());
                SharedPreferences myPreferences
                        = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                final SharedPreferences.Editor myEditor = myPreferences.edit();
                myEditor.putString("login",username.getText().toString());
                myEditor.putString("password",password.getText().toString());
                myEditor.commit();
                Callback<ResponseBody> callback = new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String s = response.body().string();
                            if (!s.equals("ok"))
                            {
                                Toast.makeText(getBaseContext(),"Неправильный логин или пароль",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                String sid = response.raw().headers().get("set-cookie");
                                String d = sid.split(" ")[0].split("=")[1];
                                int c = 0;

                                Headers headers = response.raw().headers();
                                for (int i = 0; i < headers.size(); i++) {
                                    if (headers.name(i).equals("Set-Cookie"))
                                    {
                                        if (c==1)
                                        {
                                            cookie_data = cookie_data + headers.value(i).split(" ")[0].replace(";","");

                                        }
                                        else {
                                            cookie_data = cookie_data + headers.value(i).split(" ")[0];
                                        }
                                        c++;
                                    }
                                    Log.d("cookie",headers.value(i));
                                    Log.d("cookie",headers.name(i));
                                }
                                myEditor.putString("cookie",cookie_data);
                                myEditor.commit();
                                Intent i = new Intent();
                                i.putExtra("cookie",cookie_data);
                                setResult(1,i);
                                finish();

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {
                        Log.e("tag","help");
                    }
                };
                call.enqueue(callback);
            }
        });
    }
}
