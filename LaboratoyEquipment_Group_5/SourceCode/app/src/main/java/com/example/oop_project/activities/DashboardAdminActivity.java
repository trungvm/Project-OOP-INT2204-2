package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.oop_project.R;
import com.example.oop_project.databinding.ActivityDashboardAdminBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DashboardAdminActivity extends AppCompatActivity {
    public ActivityDashboardAdminBinding binding;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        binding.layoutEquipments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, EquipmentManagerActivity.class));
                finish();
            }
        });
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Intent intent = new Intent(DashboardAdminActivity.this, DashboardAdminActivity.class);
                        intent.putExtra("CURRENT_TAB", 0);
                        startActivity(intent);
                        finish(); //
                        return true;
                    case R.id.menuAccount:
                        Intent intent1 = new Intent(DashboardAdminActivity.this, ProfileActivity.class);
                        intent1.putExtra("CURRENT_TAB", 2);
                        startActivity(intent1);
                        item.setChecked(true);
                        finish(); //
                        return true;
                }
                return false;
            }
        });
        binding.layoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, BorrowsAdminActivity.class));
                finish();
            }
        });
        binding.layoutSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, ScheduleAdminActivity.class));
                finish();
            }
        });
    }
    String name = "";
    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            String email = firebaseUser.getEmail();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String profileImage = "" + snapshot.child("profileImage").getValue();
                            if(!profileImage.equals("null") && !isDestroyed()){
                                Glide.with(DashboardAdminActivity.this)
                                        .load(profileImage)
                                        .placeholder(R.drawable.ic_person_gray)
                                        .into(binding.profileTv);
                            }
                            name = ""+snapshot.child("fullName").getValue();
                            if(name.equals("null")){
                                binding.textUserName.setText(email);
                            }else{
                                binding.textUserName.setText(name);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}