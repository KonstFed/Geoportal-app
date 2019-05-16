package com.example.biologic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private String url;
    private ImageView logo;

    private ArrayList<Table> tables = new ArrayList<Table>();
    public JSONArray columns;
    public Table table;
    Retrofit retrofit;
    Context context;
    @SuppressLint("ResourceType")
    interface GeoportalConnect
    {
        @GET("/dataset/list/")
        Call<ResponseBody> getStruct(@Query("f") String f, @Query("iDisplayStart") int iDisplayStart, @Query("iDisplayLength") int iDisplayLength, @Query("s_fields") String s_fields, @Query("f_id") int f_id);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = this.getResources();
        url = res.getString(R.string.urlstruct);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.testlogoussr);
        context = this;
        if (isOnline(this)) {
            String[] ur = new String[1];
            ur[0] = url;
        } else {
            Toast.makeText(this, res.getString(R.string.noInternet), LENGTH_LONG).show();

        }

        retrofit = new Retrofit.Builder()
                .baseUrl(url) // адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getStructInterface("100",0,100,"JSON",2206);

    }
    private void getStructInterface(String f,int iDisplayStart,int iDisplayLength,String s_fields,int f_id)
    {

        GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
        Call<ResponseBody> call = geoportalConnect.getStruct(f,iDisplayStart,iDisplayLength,s_fields,f_id);
        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String register = response.body().string(); // получили ответ в виде объекта

                    JSONObject struct = new JSONObject(register);
                    JSONArray aadata = struct.getJSONArray("aaData");
                    if (aadata.length() == 1) {
                        JSONObject tmp = aadata.getJSONObject(0);
                        String tabledesc = tmp.getString("JSON");
                        JSONObject struct2 = new JSONObject(tabledesc);
                        columns = struct2.getJSONArray("columns");
                        makeInterface(columns);
                    }


                } catch (JSONException e) {
                    Log.d("serverResponce", "struct dnt parse");

                }
                catch (IOException e)
                {
                    Log.d("","");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // обрабатываем ошибку, если она возникла
                Toast.makeText(context,"Ошибка соедининия, попробуйте снова",Toast.LENGTH_LONG).show();

            }
        };
        call.enqueue(callback);
    }
    private void makeInterface(JSONArray columns)
    {
        for (int i = 0; i < 10; i++) {
            ArrayList<Note> noteb = new ArrayList<Note>();
            for (int j = 0; j < 10; j++) {
                Note tmp1 = new Note("note" + i + "/" + j, null);
                noteb.add(tmp1);
            }
            table = new Table("product" + i, columns, noteb);
            tables.add(table);
        }
        createTablesSpinner(tables);

        NotesAdapter notesAdapter = new NotesAdapter(getBaseContext(), tables.get(0));
        ListView ar = (ListView) findViewById(R.id.notes);
        ar.setAdapter(notesAdapter);
    }


    protected void createTablesSpinner(final ArrayList<Table> tables)
    {
        ArrayAdapter<Table> arrayAdapter = new ArrayAdapter<Table>(this,android.R.layout.simple_spinner_item,tables);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.tables);
        spinner.setAdapter(arrayAdapter);
        spinner.setPrompt("Таблицы");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(),"position =  " +position, LENGTH_LONG).show();
                if (position!=0)
                {
                    NotesAdapter notesAdapter = new NotesAdapter(getBaseContext(),tables.get(position));
                    ListView ar = (ListView) findViewById(R.id.notes);
                    ar.setAdapter(notesAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
