package com.example.healthfirst;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class BMI extends AppCompatActivity {
    Button calculate_btn;
    Button reCalculate;
    EditText etHeight;
    EditText etWeight;
    TextView bmi_tv;
    TextView status;
    TextView bmi;
    private static final DecimalFormat decfor = new DecimalFormat("0.00");
    static public boolean active = false;
    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_caculator);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        calculate_btn = (Button) findViewById(R.id.calculate_btn);
        reCalculate = (Button) findViewById(R.id.reCalculate);
        etHeight = (EditText) findViewById(R.id.etHeight);
        etWeight = (EditText) findViewById(R.id.etWeight);
        bmi_tv = (TextView) findViewById(R.id.bmi_tv);
        status = (TextView) findViewById(R.id.status);
        bmi = (TextView) findViewById(R.id.bmi);
        calculate_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!etHeight.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty()) {
                    int height = Integer.parseInt(etHeight.getText().toString());
                    int weight = Integer.parseInt(etWeight.getText().toString());

                    float BMI = calculateBMI(height, weight);

                    bmi.setText(decfor.format(BMI));
                    bmi.setVisibility(View.VISIBLE);

                    if (BMI < 18.5) {
                        status.setText("Under Weight");
                    } else if (BMI >= 18.5 && BMI < 24.9) {
                        status.setText("Healthy");
                    } else if (BMI >= 24.9 && BMI < 30) {
                        status.setText("Overweight");
                    } else if (BMI >= 30) {
                        status.setText("Suffering from Obesity");
                    }

                    bmi_tv.setVisibility(View.VISIBLE);
                    status.setVisibility(View.VISIBLE);

                    reCalculate.setVisibility(View.VISIBLE);
                    calculate_btn.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter the valid height and weight", Toast.LENGTH_SHORT).show();

                }
            }

        });
        reCalculate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ResetEverything();
            }
        });
    }





    private void ResetEverything() {

        calculate_btn.setVisibility(View.VISIBLE);
        reCalculate.setVisibility(View.GONE);

        etHeight.setText("");
        etWeight.setText("");
        status.setText(" ");
        bmi.setText(" ");

        bmi_tv.setVisibility(View.GONE);
    }
    private float calculateBMI(int height,int weight){

        float Height_in_metre = (float) height / 100;
        float BMI = (float) weight / (Height_in_metre * Height_in_metre);

        return BMI;
    }
}
