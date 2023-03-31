package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.oop_project.R;
import com.example.oop_project.databinding.ActivityDashboardUserBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardUserActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ActivityDashboardUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        // handle click logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
        binding.layoutEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardUserActivity.this, EquipmentUserShowActivity.class));
            }
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Intent intent = new Intent(DashboardUserActivity.this, DashboardUserActivity.class);
                        intent.putExtra("CURRENT_TAB", 0);
                        startActivity(intent);
                        finish(); // optional, để đóng MainActivity khi chuyển sang HomeActivity
                        return true;
                    case R.id.menuAccount:
                        Intent intent1 = new Intent(DashboardUserActivity.this, ProfileActivity.class);
                        intent1.putExtra("CURRENT_TAB", 2);
                        startActivity(intent1);
                        item.setChecked(true);
                        finish(); // optional, để đóng MainActivity khi chuyển sang HomeActivity
                        return true;
                }
                return false;
            }
        });

    }
    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            binding.textUserName.setText("Anonymous");
        } else {
            String email = firebaseUser.getEmail();
            binding.textUserName.setText(email);
        }
    }
}