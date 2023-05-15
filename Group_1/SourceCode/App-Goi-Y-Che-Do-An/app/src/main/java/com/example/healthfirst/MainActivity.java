package com.example.healthfirst;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{

    ImageButton i1;
    ImageButton i2;
    ImageButton i3;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);



        setContentView(R.layout.activity_main);setContentView(R.layout.activity_main);
        i1 = (ImageButton) findViewById(R.id.imageButton);
        i2 = (ImageButton) findViewById(R.id.imageButton2);
        i3 = (ImageButton) findViewById(R.id.imageButton4);


        i1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent x = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(x);
            }
        });

        i2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent x = new Intent(MainActivity.this, MainActivity3.class);
                startActivity(x);
            }
        });

        i3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(MainActivity.this, MainActivity7.class);
                startActivity(x);
            }
        });
    }

    public void onClick(View view) {
        Intent x = new Intent(MainActivity.this, BMI.class);
        startActivity(x);
    }
}