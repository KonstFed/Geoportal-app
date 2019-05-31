package com.example.modul2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button hex = (Button) findViewById(R.id.hex);
        hex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) findViewById(R.id.editText);
                int val =Integer.parseInt(ed.getText().toString());
                Toast.makeText(getApplicationContext(),Integer.toHexString(val),Toast.LENGTH_LONG).show();
            }
        });
        Button oct = (Button) findViewById(R.id.oct);
        oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ed = (EditText) findViewById(R.id.editText);
                int val = Integer.parseInt(ed.getText().toString());
                Toast.makeText(getApplicationContext(),Integer.toOctalString(val),Toast.LENGTH_LONG).show();
            }
        });
    }
}
