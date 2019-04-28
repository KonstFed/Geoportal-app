package com.example.biologic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {
    private String getStructURL;
    private String url;
    private ImageView logo;
    private boolean isRegged;
    private ArrayList<Table> tables = new ArrayList<Table>();

    RelativeLayout mainLayer;
    @SuppressLint("ResourceType")
    interface GeoportalConnect
    {
        @GET("/")
        Call<Response> getStruct();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = this.getResources();
        url = res.getString(R.string.urlstruct);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.testlogoussr);
//
//
//        if (isOnline(this)) {
//            String[] ur = new String[1];
//            ur[0] = url;
//            ConnectServ serverConnect = new ConnectServ("structure",this);
//            serverConnect.execute(ur);
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(getStructURL) // адрес сервера
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        else
//        {
//            Toast.makeText(this,res.getString(R.string.noInternet),LENGTH_LONG).show();
//
//        }
        for (int i = 0; i < 10; i++) {
            ArrayList<Note> noteb = new ArrayList<Note>();
            for (int j = 0; j <10 ; j++) {
                Note tmp1 = new Note("note"+i+"/"+j,null);
                noteb.add(tmp1);
            }
            Table tmp = new Table("product"+i,null,noteb);
            tables.add(tmp);
        }
        createTablesSpinner(tables);
        ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Note nots = new Note("note"+i,null);
            notes.add(nots);
        }

        NotesAdapter notesAdapter = new NotesAdapter(getBaseContext(),tables.get(0));
        ListView ar = (ListView) findViewById(R.id.notes);
        ar.setAdapter(notesAdapter);
    }
//    public void drawform(JSONObject struct)
//    {
//        mainLayer = (RelativeLayout) findViewById(R.id.mainactivity);
////        LayoutInflater inflater = getLayoutInflater();
//
//        try {
//            JSONArray columns = struct.getJSONArray("columns");
//            TextView f = (TextView) findViewById(R.id.ssa);
//            for (int i = 0; i < columns.length(); i++) {
//                JSONObject column = columns.getJSONObject(i);
//                f.setText(f.getText()+" "+ column.getString("fieldname") + "///////////////////");
//                if( column.has("widget")) {
//                    JSONObject widget = column.getJSONObject("widget");
//                    String widgetname = widget.getString("name");
//
//                    switch (widgetname)
//                    {
//                        case "edit":
////                            EditText field = inflater.inflate(R.layout.fieldedit);
//                            EditText field = new EditText(this);
//
//                            RelativeLayout.LayoutParams editviewparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                            field.setLayoutParams(editviewparams);
//                            mainLayer.addView(field);
//                            //Создаем поле ввода (нормальное)
//                            break;
//                        case "data":
//                            //типо календарь
//                            break;
//                        case "number":
//                            // число
//                            break;
//                    }
//                }
//            }
//
//
//        }
//        catch (JSONException e) {
//        e.printStackTrace();
//    }
//    }
    public void onMyButtonClick(View view)
    {
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
                Toast.makeText(getBaseContext(),"position =  " +position,Toast.LENGTH_LONG).show();
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
