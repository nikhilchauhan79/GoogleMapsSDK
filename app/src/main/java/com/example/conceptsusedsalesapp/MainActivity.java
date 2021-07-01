package com.example.conceptsusedsalesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    TextInputEditText textInputEditText_1,textInputEditText_2;
    Button button,mapsButton,locationButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputEditText_1=findViewById(R.id.et_1);
        textInputEditText_2=findViewById(R.id.et_2);
        button=findViewById(R.id.alarm_button);
        locationButton=findViewById(R.id.location_button);
        mapsButton=findViewById(R.id.maps_button);


        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.name), "nikhil chauhan");
        editor.apply();

        String nameFromDisk = sharedPref.getString(getString(R.string.name), "default name");
        Log.d("name", "onCreate: "+nameFromDisk);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,AlarmActivity.class);
                startActivity(intent);
            }
        });

        mapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MapsLocationActivity.class);
                startActivity(intent);
            }
        });






    }
}