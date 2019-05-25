package com.example.biologic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    private String url;
    private ImageView logo;
    private ArrayList<Table> tables = new ArrayList<Table>();
    private float dy;
    private float dx;

    public float dp;
    public JSONArray columns;
    public String f="2206";
    public String s_fields = "";
    public ArrayList<Widget> widgetsArray = new ArrayList<>();
    public Table table;

    protected Retrofit retrofit;
    protected Context context;
    @SuppressLint("ResourceType")
    interface GeoportalConnect
    {
        @GET("/dataset/list/")
        Call<ResponseBody> getStruct(@Query("f") String f, @Query("iDisplayStart") int iDisplayStart, @Query("iDisplayLength") int iDisplayLength, @Query("s_fields") String s_fields, @Query("f_id") String f_id);
        @GET("/dataset/list/")
        Call<ResponseBody> getData(@Query("f") String f, @Query("iDisplayStart") int iDisplayStart, @Query("iDisplayLength") int iDisplayLength, @Query("s_fields") String s_fields);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = this.getResources();
        url = res.getString(R.string.urlstruct);
        context = getApplicationContext();
        retrofit = new Retrofit.Builder()
                .baseUrl(url) // адрес сервера
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if (isOnline(getApplicationContext())) {
            getStructInterface(0, 100);
        }
        else Toast.makeText(this,R.string.noInternet,Toast.LENGTH_LONG);
        dp = getResources().getDisplayMetrics().density;
        dx = getResources().getDisplayMetrics().heightPixels;
        dy = getResources().getDisplayMetrics().widthPixels;

    }
    private void getStructInterface(int iDisplayStart,int iDisplayLength)
    {

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
                                            widget = new W_edit(tmp1, getApplicationContext());

                                            break;
                                        case "date":
                                            widget = new W_date(tmp1, getApplicationContext());

                                            break;
                                        case "number":
                                            widget = new W_number(tmp1, getApplicationContext());

                                            break;
                                    }
                                    widgetsArray.add(widget);
                                }
                                else
                                {
                                    if (tmp1.getString("fieldname").equals("id"))
                                    {
                                        Widget w_id = new W_number(tmp1,getApplicationContext());
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
        int id =-1;
        for (int i = 0; i < widgetsArray.size(); i++) {

            Widget current = widgetsArray.get(i);
            if (current.propertyName.equals("id"))
            {
                id =Integer.parseInt(map.get(current.propertyName));
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
        OnClickListennerDeleteNote onclick = new OnClickListennerDeleteNote();
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

        GeoportalConnect geoportalConnect = retrofit.create(GeoportalConnect.class);
        Call<ResponseBody> call = geoportalConnect.getData(f,iDisplayStart,iDisplayLength,s_fields);
        Callback<ResponseBody> callback = new Callback<ResponseBody>() {
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

                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t){
            }
        };
        call.enqueue(callback);
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
