package com.example.biologic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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

public class NoteRedactorActivity extends AppCompatActivity {
    JSONArray struct;
    Retrofit retrofit;
    String url ="http://biodiv.isc.irk.ru";
    String cookie;
    private String id="";
    private String f ;
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<Widget> widgets = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2)
        {
            cookie = data.getStringExtra("cookie");
            setSetting();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_redactor);
        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        f = getIntent().getStringExtra("f");
        cookie = myPreferences.getString("cookie","");
        if (cookie.equals("")) {
            Intent i = new Intent(this, Registration.class);
            startActivityForResult(i, 2);
        }
        else
        {
            setSetting();
        }
       // struct = JSONObject(datas[1]);

    }

    private void setSetting()
    {
        Intent intent = getIntent();
        String datas = intent.getStringExtra("columns");
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

        data = intent.getStringArrayListExtra("data");
        id = intent.getStringExtra("id");
        Button buttonAccept = findViewById(R.id.acceptButton);
        Button buttonCancel = findViewById(R.id.cancelButton);

        retrofit = new Retrofit.Builder()
                .baseUrl(url) // адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = "{\"f_id\":\""+id+"\"";
                    for (int i = 0; i < widgets.size(); i++) {
//                        if (!result.equals("{")) {
                        result = result + ",";
//                        }
                        Widget cur = widgets.get(i);
                        result = result + "\"" + cur.propertyName + "\":\"" + cur.getValue()+"\"";
                    }


                    result =result + "}";
                    GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
                    Call<ResponseBody> call;
                    if(id.equals(""))  call = geoportalConnect.sendData(f, result);
                    else  call = geoportalConnect.updateData(f, result);

                    Callback<ResponseBody> callback3 = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String status = response.body().string();
                                JSONObject resp = new JSONObject(status);
                                if (resp.getString("status").equals("ok")) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    getApplicationContext().startActivity(intent);

                                } else {
                                    Toast.makeText(getApplicationContext(), getString(R.string.errorSendData), Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                            } catch (JSONException e) {
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), getString(R.string.errorServer), Toast.LENGTH_SHORT).show();

                        }
                    };

                    call.enqueue(callback3);

                }
                catch (Exception e){
                    Log.e("exceptiom","help");
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });

        try {
            struct = new JSONArray(datas);
            int cnt = 0;
            for (int i = 0; i < struct.length(); i++) {

                JSONObject tmp =struct.getJSONObject(i);
                if (tmp.has("widget") && tmp.getString("visible").equals("true")) {
                    JSONObject widgetProperty = tmp.getJSONObject("widget");
                    String widgetName = widgetProperty.getString("name");
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout scrollLayout = findViewById(R.id.ScrollData);
                    Widget widget=null;
                    switch (widgetName) {
                        case "edit":
                            widget = new W_edit(tmp, this,getResources());
                            break;
                        case "date":
                            widget = new W_date(tmp, this,getResources());
                            break;
                        case "number":
                            widget = new W_number(tmp, this,getResources());
                            break;
                        case "point":
                            widget = new W_point(tmp,this,getResources(),(LocationManager) getSystemService(Context.LOCATION_SERVICE));
                            break;
                    }
                    if (data.size()!=0 && !data.get(cnt).equals("") && !data.get(cnt).equals("null"))
                    {
                        widget.control(scrollLayout,data.get(cnt));
                    }
                    else
                    {
                        widget.control(scrollLayout);
                    }
                    widgets.add(widget);
                    cnt++;
                }
            }
//            WidgetAdapter widgetAdapter = new WidgetAdapter(widgets,this);
//            ListView fieldsListView = findViewById(R.id.listviewFields);
//            fieldsListView.setAdapter(widgetAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        // struct = JSONObject(
    }
}
