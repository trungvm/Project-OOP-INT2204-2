package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.oop_project.R;
import com.example.oop_project.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int currentTab = getIntent().getIntExtra("CURRENT_TAB", 0);
        binding.bottomNavigationView.getMenu().getItem(currentTab).setChecked(true);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Intent intent = new Intent(ProfileActivity.this, DashboardUserActivity.class);
                        intent.putExtra("CURRENT_TAB", 0);
                        startActivity(intent);
                        item.setChecked(true);
                        finish(); // optional, để đóng MainActivity khi chuyển sang HomeActivity
                        return true;
                    case R.id.menuAccount:
                        Intent intent1 = new Intent(ProfileActivity.this, ProfileActivity.class);
                        intent1.putExtra("CURRENT_TAB", 2);
                        startActivity(intent1);
                        item.setChecked(true);
                        ColorStateList selectedColor = ColorStateList.valueOf(ContextCompat.getColor(ProfileActivity.this, R.color.green));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            item.setIconTintList(selectedColor);
                        }
                        finish(); // optional, để đóng MainActivity khi chuyển sang HomeActivity
                        return true;
                }
                return false;
            }
        });
    }
}