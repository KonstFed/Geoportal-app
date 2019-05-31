package com.example.biologic;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class W_point extends Widget {
    class MyLocationListener  implements LocationListener {

        String lon="";
        String lat="";
        @Override
        public void onLocationChanged(final Location location) {
            lon = "" + location.getLongitude();
            lat = "" + location.getLatitude();
            longitude.setText(lon);
            latitude.setText(lat);

            /*------- To get city name from coordinates -------- */
//        String cityName = null;
//        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(location.getLatitude(),
//                    location.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                System.out.println(addresses.get(0).getLocality());
//                cityName = addresses.get(0).getLocality();
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                + cityName;
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
    EditText latitude;
    EditText longitude;
    LocationManager locationManager;
    LinearLayout layoutForm;
    public W_point(JSONObject field, Context context, Resources res,LocationManager locationManager) {
        super(field, context, res);
        this.locationManager = locationManager;
        longitude = new EditText(context);
        latitude = new EditText(context);
        layoutForm = new LinearLayout(context);
        layoutForm.setOrientation(LinearLayout.HORIZONTAL);
    }

    @Override
    public String getValue() {
        return "MULTIPOINT("+longitude.getText()+" "+latitude.getText()+")";
    }

    @Override
    public void createView(LinearLayout linearLayout, String value) {
        String[] val = value.replace("MULTIPOINT","").split(" ");

        String longitude_val = val[0].replace("(","");
        String latitude_val = val[1].replace(")","");
        LinearLayout lin = new LinearLayout(context);
        lin.setOrientation(LinearLayout.VERTICAL);
        TextView text = new TextView(context);
        text.setTextSize(30);
        text.setText(propertyName + ":");
        lin.addView(text);
        TextView longitude = new TextView(context);
        longitude.setText("Долгота: " + longitude_val);// Как подавать координаты?
        longitude.setTextSize(30);
        TextView latitude = new TextView(context);
        latitude.setTextSize(30);
        latitude.setText("Широта: " + latitude_val);
        lin.addView(longitude);
        lin.addView(latitude);
        linearLayout.addView(lin);
    }

    @Override
    public void control(LinearLayout layout, String data) {
        String[] val = data.replace("MULTIPOINT","").split(" ");

        String longitude_val = val[0].replace("(","");
        String latitude_val = val[1].replace(")","");
        latitude.setText(latitude_val);
        longitude.setText(longitude_val);
        setCoordinates(layout);
    }
    private void setCoordinates(LinearLayout layout)
    {
        longitude.setTextSize(20);
        latitude.setTextSize(20);
        latitude.setHint("Широта");
        longitude.setHint("Долгота");
        latitude.setInputType(InputType.TYPE_CLASS_NUMBER);
        longitude.setInputType(InputType.TYPE_CLASS_NUMBER);
        layoutForm.addView(latitude);
        layoutForm.addView(longitude);
        TextView text = new TextView(context);
        text.setText(titlename);
        text.setTextSize(25);
        LinearLayout.LayoutParams paraBut = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paraBut.weight = 3;
        Button b = new Button(context);
        b.setBackground(res.getDrawable(R.drawable.custombutton));
        b.setText("Свои координаты");
        b.setTextSize(20);
        b.setLayoutParams(paraBut);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationListener locationListener = new MyLocationListener();

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                } else {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

                }
            }
        });
        LinearLayout.LayoutParams paraText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paraText.setMargins(3,2,2,3);
        paraText.weight = 2;
        text.setLayoutParams(paraText);
        LinearLayout layout2 = new LinearLayout(context);
        layout2.setOrientation(LinearLayout.HORIZONTAL);
        layout2.addView(text);
        layout2.addView(b);
        LinearLayout lil = new LinearLayout(context);
        lil.setOrientation(LinearLayout.VERTICAL);
        lil.addView(layout2);
        lil.addView(layoutForm);
        lil.setBackground(res.getDrawable(R.drawable.customborder));
        layout.addView(lil);

    }
    @Override
    public void control(LinearLayout layout) {



        setCoordinates(layout);



    }
}
