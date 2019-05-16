package com.example.biologic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class NoteRedactorActivity extends AppCompatActivity {
    JSONArray struct;
    Retrofit retrofit;
    String url ="http://biodiv.isc.irk.ru";
    private ArrayList<Widget> widgets = new ArrayList<>();
    interface DataSend
    {
        @POST("/dataset/add/")
        Call<ResponseBody> sendData(@Query("f") String f, @Body RequestData document);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_redactor);
        Intent intent = getIntent();
        final String[] datas = intent.getStringExtra("Note").split("%%/");
        Button buttonAccept = findViewById(R.id.acceptButton);
        Button buttonCancel = findViewById(R.id.cancelButton);

        retrofit = new Retrofit.Builder()
                .baseUrl(url) // адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String result = "{";
                    for (int i = 0; i < widgets.size(); i++) {
                        if (!result.equals("{")) {
                            result = result + ",";
                        }
                        Widget cur = widgets.get(i);
                        result = result + cur.propertyName + ":\"" + cur.getValue()+"\"";
                    }


                    result ="{document:"+ result + "}}";
                    JSONObject request = new JSONObject(result);
                    RequestData requestData = new RequestData(request);
                    JSONObject rq = new JSONObject();

                    DataSend dataSend = retrofit.create(DataSend.class);
                    Call<ResponseBody> call = dataSend.sendData("2206",requestData );
                    Callback<ResponseBody> callback = new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String status = response.body().string();
                                JSONObject resp  = new JSONObject(status);
                                if (resp.getString("status").equals("ok"))
                                {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    getApplicationContext().startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),getString(R.string.errorSendData),Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {}
                            catch (JSONException e){}
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),getString(R.string.errorServer),Toast.LENGTH_SHORT).show();

                        }
                    };
                    call.enqueue(callback);

                }
                catch (JSONException e){}
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
            struct = new JSONArray(datas[1]);
            for (int i = 0; i < struct.length(); i++) {

                JSONObject tmp =struct.getJSONObject(i);
                if (tmp.has("widget")) {
                    JSONObject widgetProperty = tmp.getJSONObject("widget");
                    String widgetName = widgetProperty.getString("name");
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                    LinearLayout linearLayout = findViewById(R.id.layout);
                    switch (widgetName) {
                        case "edit":
                            W_edit w_edit = new W_edit(tmp, this, layoutParams);
                            widgets.add(w_edit);
                            break;
                        case "date":
                            W_date w_date = new W_date(tmp, this, layoutParams);
                            widgets.add(w_date);
                        case "number":
                            W_number w_number = new W_number(tmp, this, layoutParams);
                            widgets.add(w_number);

                    }
                }
            }
            WidgetAdapter widgetAdapter = new WidgetAdapter(widgets,this);
            ListView fieldsListView = findViewById(R.id.listviewFields);
            fieldsListView.setAdapter(widgetAdapter);



        } catch (JSONException e) {
            e.printStackTrace();
        }
       // struct = JSONObject(datas[1]);

    }


}
