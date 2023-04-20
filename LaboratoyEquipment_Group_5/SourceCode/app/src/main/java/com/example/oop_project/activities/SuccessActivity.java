package com.example.oop_project.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.oop_project.databinding.ActivitySuccessBinding;

public class SuccessActivity extends AppCompatActivity {
    private ActivitySuccessBinding binding;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(getIntent().getStringExtra("status") != null){
            status = getIntent().getStringExtra("status");
            if(status.equals("True")){
                binding.textV.setText("Xác nhận thành công");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SuccessActivity.this, AddRulesAdminActivity.class));
                        finish();
                    }
                }, 2000);
            }else{
                binding.textV.setText("Xác nhận thành công");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SuccessActivity.this, ScheduleAdminActivity.class));
                        finish();
                    }
                }, 2000);
            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SuccessActivity.this, EquipmentsBorrowedActivity.class));
                    finish();
                }
            }, 2000);
        }

    }
}