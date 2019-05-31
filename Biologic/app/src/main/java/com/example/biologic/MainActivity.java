package com.example.biologic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
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


public class MainActivity extends AppCompatActivity {
    public class OnClickListennerDeleteNote implements View.OnClickListener {
        String id;
        String f;
        String url;
        MainActivity mainActivity;
        public OnClickListennerDeleteNote(String id, String f,String url,MainActivity mainActivity) {
            this.id = id;
            this.f = f;
            this.url = url;
            this.mainActivity = mainActivity;
        }

        @Override
        public void onClick(View v) {

            String document = "{\"f_id\":\""+ id + "\"}";
            GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
            Call<ResponseBody> call = geoportalConnect.deleteData(f,document);
            Callback<ResponseBody> callback = new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String status = response.body().string();
                        JSONObject resp = new JSONObject(status);
                        drawData(0,100);

                    }catch (Exception e){
                        drawData(0,100);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    drawData(0,100);

                }
            };
            call.enqueue(callback);

        }
    }

    private String url;
    private ImageView logo;
    private ArrayList<Table> tables = new ArrayList<Table>();
    private float dy;
    private float dx;

    public float dp;
    public JSONArray columns;
    public String f;
    public String s_fields = "";
    public ArrayList<Widget> widgetsArray = new ArrayList<>();
    public Table table;
    public HashMap<String,String> tables_map;
    public String cookie;
    SharedPreferences myPreferences;
    protected Retrofit retrofit;
    protected Context context;
    @SuppressLint("ResourceType")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            myPreferences    = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        cookie = myPreferences.getString("cookie","");
        f = myPreferences.getString("f","");
        if (cookie.equals(""))
        {
            Intent i = new Intent(this,Registration.class);
            startActivityForResult(i,2);
        }
        else {

            setSettings();
        }


    }

    public void setSettings()
    {
        Button menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SettingActvity.class);
                i.putExtra("tables_map", tables_map);
                i.putExtra("f",f);
                startActivityForResult(i, 1);

            }
        });

        Resources res = this.getResources();
        url = res.getString(R.string.urlstruct);
        context = getApplicationContext();
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
                .baseUrl(url) // адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
        if (isOnline(getApplicationContext())) {
//            getTables(0, 100);
            getStructInterface(0, 100);
        } else Toast.makeText(this, R.string.noInternet, Toast.LENGTH_LONG);
        dp = getResources().getDisplayMetrics().density;
        dx = getResources().getDisplayMetrics().heightPixels;
        dy = getResources().getDisplayMetrics().widthPixels;
        ImageButton new_note_button = findViewById(R.id.newNoteButton);
        ViewGroup.LayoutParams para = new_note_button.getLayoutParams();
        TextView text = findViewById(R.id.nameUser);
        para.height = (int) dx / 13;
        para.width = para.height;
        new_note_button.setLayoutParams(para);
        new_note_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NoteRedactorActivity.class);
                ArrayList<String> ar = new ArrayList<>();
                i.putExtra("data", ar);
                i.putExtra("f",f);
                i.putExtra("id", "");
                i.putExtra("columns", columns.toString());
                startActivity(i);


            }
        });

        Log.e("dsa", "dsada");
    }

    public void getTables(int iDisplayStart,int iDisplayLength)
    {
        GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
        Call<ResponseBody> call = geoportalConnect.getData(Integer.toString(100),iDisplayStart,iDisplayLength,"title");
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
                        tables_map.put(tmp.getString("name"),tmp.getString("id"));
                        if (i==0) f = tmp.getString("id");
                    }
                    Log.e("e","dsada");
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("e","dsada");

            }
        };
        call.enqueue(callback);
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 2:
                cookie = data.getStringExtra("cookie");
                setSettings();
            case 1:

                String newf = data.getStringExtra("f");

                if(newf!=f) {
                    f = newf;
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    myEditor.putString("f", f);
                    myEditor.commit();
                    getStructInterface(0, 100);
                }
                else
                {
                    drawData(0,100);
                }
        }
    }

    public void getStructInterface(int iDisplayStart, int iDisplayLength)
    {
        widgetsArray = new ArrayList<>();
        LinearLayout lia = findViewById(R.id.NotesContainer);
        lia.removeAllViews();
        if (f.equals("")) return;
        GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
        Call<ResponseBody> call = geoportalConnect.getStruct("100",iDisplayStart,iDisplayLength,"JSON",f);
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

                        for (int i = 0; i < columns.length(); i++) {
                            JSONObject tmp1 = columns.getJSONObject(i);

                            if (tmp1.has("widget")) {
                                if (tmp1.getString("visible").equals("true"))
                                {
                                    Widget widget = null;

                                    JSONObject widgetProperty = tmp1.getJSONObject("widget");
                                    String widgetName = widgetProperty.getString("name");
                                    switch (widgetName) {
                                        case "edit":
                                            widget = new W_edit(tmp1, getApplicationContext(), getResources());

                                            break;
                                        case "date":
                                            widget = new W_date(tmp1, getApplicationContext(),getResources());

                                            break;
                                        case "number":
                                            widget = new W_number(tmp1, getApplicationContext(),getResources());

                                            break;
                                        case "point":
                                            widget = new W_point(tmp1,getApplicationContext(),getResources(),(LocationManager) getSystemService(Context.LOCATION_SERVICE));
                                    }
                                    widgetsArray.add(widget);
                                }
                                else
                                {
                                    if (tmp1.getString("fieldname").equals("id"))
                                    {
                                        Widget w_id = new W_number(tmp1,getApplicationContext(),getResources());
                                        widgetsArray.add(w_id);
                                    }
                                }
                            }
                        }

                        drawData(0,8);
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
    public void createNotes(HashMap<String,String> map)
    {

        LinearLayout scrollLayout = findViewById(R.id.NotesContainer);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout lin = new LinearLayout(getApplicationContext());
        lin.setOrientation(LinearLayout.VERTICAL);
        int marginAll = (int) dp * 15;
        params.setMargins(marginAll,(int)marginAll/2,marginAll,(int)marginAll/2);
        lin.setLayoutParams(params);

        Resources res = getResources();
        lin.setBackground(res.getDrawable(R.drawable.customborder));
        ArrayList<String> dataString = new ArrayList<>();
        String id="";
        for (int i = 0; i < widgetsArray.size(); i++) {

            Widget current = widgetsArray.get(i);
            if (current.propertyName.equals("id"))
            {
                id =map.get(current.propertyName);
            }
            else
            {
                current.createView(lin, map.get(current.propertyName));
                dataString.add(map.get(current.propertyName));
            }
        }
        LinearLayout linButtons = new LinearLayout(getBaseContext());
        linButtons.setOrientation(LinearLayout.HORIZONTAL);

        ImageButton redactButton = new ImageButton(getApplicationContext());
        redactButton.setBackground(getResources().getDrawable(R.drawable.redactbutton));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = (int) dx/13;
        layoutParams.height =(int) dx/13;
        redactButton.setLayoutParams(layoutParams);
        redactButton.setOnClickListener(new OnClickListennerRedactActivity(id,dataString,columns,getBaseContext()));
        linButtons.addView(redactButton);

        ImageButton deleteButton = new ImageButton(getBaseContext());
        deleteButton.setBackground(getResources().getDrawable(R.drawable.deletebutton));
        OnClickListennerDeleteNote onclick = new OnClickListennerDeleteNote(id,f,getResources().getString(R.string.urlstruct),this);
        LinearLayout.LayoutParams layoutParamsDelete = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsDelete.width = (int) dx/13;
        layoutParamsDelete.height =(int) dx/13;
        layoutParamsDelete.leftMargin = 24;
        deleteButton.setLayoutParams(layoutParamsDelete);
        deleteButton.setOnClickListener(onclick);
        linButtons.addView(deleteButton);
        lin.addView(linButtons);
        scrollLayout.addView(lin);
    }

    public void drawData(int iDisplayStart,int iDisplayLength)
    {
        if(f=="")
            return;
        LinearLayout lin= findViewById(R.id.NotesContainer);
        lin.removeAllViews();
        GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
        Call<ResponseBody> call = geoportalConnect.getData(f,iDisplayStart,iDisplayLength,s_fields);
        Callback<ResponseBody> callback2 = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String register = response.body().string(); // получили ответ в виде объекта

                    JSONObject struct = new JSONObject(register);
                    JSONArray aadata = struct.getJSONArray("aaData");
                    for (int i = 0; i < aadata.length(); i++) {
                        JSONObject cur = aadata.getJSONObject(i);
                        HashMap<String,String> map = new HashMap<>();
                        for (int j = 0; j < widgetsArray.size(); j++) {
                            Widget curWidget = widgetsArray.get(j);
                            map.put(curWidget.propertyName,cur.getString(curWidget.propertyName));
                        }
                        createNotes(map);
                    }

                }catch (Exception e){
                    Log.e("","sad");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
                Log.e("net","g");
            }
        };
        call.enqueue(callback2);
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
