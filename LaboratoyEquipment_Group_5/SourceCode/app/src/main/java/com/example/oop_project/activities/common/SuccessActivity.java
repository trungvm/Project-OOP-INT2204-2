package com.example.oop_project.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.oop_project.activities.admin.AddRulesAdminActivity;
import com.example.oop_project.activities.admin.ScheduleAdminActivity;
import com.example.oop_project.activities.user.EquipmentsBorrowedActivity;
import com.example.oop_project.databinding.ActivitySuccessBinding;

public class SuccessActivity extends AppCompatActivity {
    private ActivitySuccessBinding binding;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getStringExtra("status") != null) {
            status = getIntent().getStringExtra("status");
            if (status.equals("True")) {
                binding.textV.setText("Xác nhận thành công");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SuccessActivity.this, AddRulesAdminActivity.class));
                        finish();
                    }
                }, 2000);
            } else if (status.equals("ScheduleRefuse")) {
                binding.textV.setText("Xác nhận hủy thành công");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SuccessActivity.this, ScheduleAdminActivity.class));
                        finish();
                    }
                }, 2000);
            } else if (status.equals("ScheduleAccept")) {
                binding.textV.setText("Xác nhận cho mượn thành công");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SuccessActivity.this, ScheduleAdminActivity.class));
                        finish();
                    }
                }, 2000);
            }
        } else {
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