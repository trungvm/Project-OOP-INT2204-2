package com.oop.iotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MaterialCardView cvUser, cvLight, cvHeater, cvDoor, cvTemperature, cvCurtain, cvCamera;
    private TextView tvWelcome;

    private String user = "USER";

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIntent = getIntent();
        email = loginIntent.getStringExtra("email");

        initViews();

        clickCardViews();
    }

    private void clickCardViews() {
        Log.d(TAG, "clickCardViews: Stared");

        cvUser.setOnClickListener(e -> {
            Intent intentUser = new Intent(MainActivity.this, null);
            //TODO click user
        });

        cvLight.setOnClickListener(e -> {
            Intent intentLight = new Intent(MainActivity.this, null);
            //TODO click Light
        });

        cvHeater.setOnClickListener(e -> {
            Intent intentHeater = new Intent(MainActivity.this, null);
            //TODO click Heater
        });

        cvTemperature.setOnClickListener(e -> {
            Intent intentTemperature = new Intent(MainActivity.this, TemperatureActivity.class);
            intentTemperature.putExtra("email", email);
            startActivity(intentTemperature);
        });

        cvDoor.setOnClickListener(e -> {
            Intent intentDoor = new Intent(MainActivity.this, null);
            //TODO click Door
        });

        cvCurtain.setOnClickListener(e -> {
            Intent intentCurtain = new Intent(MainActivity.this, null);
            //TODO click Curtain
        });

        cvCamera.setOnClickListener(e -> {
            Intent intentCamera = new Intent(MainActivity.this, null);
            //TODO click Camera
        });
    }

    private void initViews() {
        Log.d(TAG, "initViews: Started");

        cvUser = findViewById(R.id.cardview_user);
        cvLight = findViewById(R.id.cardview_light);
        cvHeater = findViewById(R.id.cardview_heater);
        cvDoor = findViewById(R.id.cardview_door);
        cvTemperature  = findViewById(R.id.cardview_temperature);
        cvCurtain = findViewById(R.id.cardview_curtain);
        cvCamera = findViewById(R.id.cardview_camera);

        tvWelcome = findViewById(R.id.textview_welcome);
        tvWelcome.setText("Welcome " + user);
    }

}