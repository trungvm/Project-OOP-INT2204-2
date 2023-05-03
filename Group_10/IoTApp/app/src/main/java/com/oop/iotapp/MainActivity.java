package com.oop.iotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MaterialCardView cvUser, cvLight, cvHeater, cvTemperature, cvCurtain;
    private TextView tvWelcome;

    private String user = "";

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIntent = getIntent();
        uid = loginIntent.getStringExtra("uid");
//        Log.d("EMAIL", email);
//        Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();

        initViews();

        clickCardViews();
    }

    private void clickCardViews() {
        Log.d(TAG, "clickCardViews: Stared");

        cvUser.setOnClickListener(e -> {
            FirebaseAuth.getInstance().signOut();
            Intent intentUser = new Intent(MainActivity.this, LoginActivity.class);
            intentUser.putExtra("uid", uid);
            startActivity(intentUser);
        });

        cvLight.setOnClickListener(e -> {
            Intent intentLight = new Intent(MainActivity.this, LightActivity.class);
            intentLight.putExtra("uid", uid);
            startActivity(intentLight);
        });

        cvHeater.setOnClickListener(e -> {
            Intent intentHeater = new Intent(MainActivity.this, HeaterActivity.class);
            intentHeater.putExtra("uid", uid);
            startActivity(intentHeater);
        });

        cvTemperature.setOnClickListener(e -> {
            Intent intentTemperature = new Intent(MainActivity.this, TemperatureActivity.class);
            intentTemperature.putExtra("uid", uid);
            startActivity(intentTemperature);
        });


        cvCurtain.setOnClickListener(e -> {
            Intent intentCurtain = new Intent(MainActivity.this, CurtainsActivity.class);
            intentCurtain.putExtra("uid", uid);
            startActivity(intentCurtain);
        });

    }

    private void initViews() {
        Log.d(TAG, "initViews: Started");

        cvUser = findViewById(R.id.cardview_user);
        cvLight = findViewById(R.id.cardview_light);
        cvHeater = findViewById(R.id.cardview_heater);
        cvTemperature = findViewById(R.id.cardview_temperature);
        cvCurtain = findViewById(R.id.cardview_curtain);

        tvWelcome = findViewById(R.id.textview_welcome);
        tvWelcome.setText("Welcome " + user);
    }

}