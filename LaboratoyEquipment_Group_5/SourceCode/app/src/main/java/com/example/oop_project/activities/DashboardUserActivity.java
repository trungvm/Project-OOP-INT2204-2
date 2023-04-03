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
    private boolean isUser = false;
    private int isLoginWithout = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isLoginWithout = getIntent().getIntExtra("WITHOUT_LOGIN", 0);
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
      if(isUser == true){
          binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
              @Override
              public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                  switch (item.getItemId()){
                      case R.id.menuHome:
                          Intent intent = new Intent(DashboardUserActivity.this, DashboardUserActivity.class);
                          intent.putExtra("CURRENT_TAB", 0);
                          startActivity(intent);
                          finish(); //
                          return true;
                      case R.id.menuFavorite:
                          Intent intent1 = new Intent(DashboardUserActivity.this, FavoriteActivity.class);
                          intent1.putExtra("CURRENT_TAB", 1);
                          startActivity(intent1);
                          finish();
                          return true;
                      case R.id.menuAccount:
                          Intent intent2 = new Intent(DashboardUserActivity.this, ProfileActivity.class);
                          intent2.putExtra("CURRENT_TAB", 2);
                          startActivity(intent2);
                          item.setChecked(true);
                          finish(); //
                          return true;
                  }
                  return false;
              }
          });
      }

    }
    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null && isLoginWithout == 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        }  else if(isLoginWithout == 1){
            binding.textUserName.setText("Anonymous");
        }else {
            isUser = true;
            String email = firebaseUser.getEmail();
            binding.textUserName.setText(email);
        }
    }
}