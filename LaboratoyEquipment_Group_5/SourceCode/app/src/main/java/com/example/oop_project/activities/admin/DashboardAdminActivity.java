package com.example.oop_project.activities.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.oop_project.R;
import com.example.oop_project.activities.common.MainActivity;
import com.example.oop_project.activities.common.ProfileActivity;
import com.example.oop_project.databinding.ActivityDashboardAdminBinding;
import com.example.oop_project.models.Person;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.noties.markwon.Markwon;

public class DashboardAdminActivity extends AppCompatActivity {
    public ActivityDashboardAdminBinding binding;
    private FirebaseAuth firebaseAuth;
    private boolean isLayoutOpen = false;
    int cnt = 0;
    String name = "";


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
                switch (item.getItemId()) {
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
            }
        });
        binding.layoutSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, ScheduleAdminActivity.class));
            }
        });
        setUpNotification();
        binding.layoutRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, AddRulesAdminActivity.class));
            }
        });
        binding.layoutRulesC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int heightInPxt = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 950, getResources().getDisplayMetrics());
                if (isLayoutOpen) {
                    binding.contentRulesSv.setVisibility(View.GONE);
                    binding.layoutRulesC.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    binding.layoutRulesC.requestLayout();
                    isLayoutOpen = false;
                    binding.layoutMain.setVisibility(View.VISIBLE);

                    int heightInPxt1 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 405, getResources().getDisplayMetrics());

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.layoutRulesC.getLayoutParams();
                    params.topMargin = heightInPxt1;
                    binding.layoutRulesC.setLayoutParams(params);
                } else {
                    binding.layoutRulesC.getLayoutParams().height = heightInPxt;
                    binding.layoutRulesC.requestLayout();
                    binding.contentRulesSv.setVisibility(View.VISIBLE);
                    binding.layoutMain.setVisibility(View.GONE);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.layoutRulesC.getLayoutParams();
                    params.topMargin = 0;
                    binding.layoutRulesC.setLayoutParams(params);
                    isLayoutOpen = true;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Rules");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final Markwon markwon = Markwon.create(DashboardAdminActivity.this);
                            String text = "" + snapshot.child("content").getValue();
                            text = text.replace("\\n", "\n");
                            String newText = text;
                            markwon.setMarkdown(binding.contentRules, newText);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
        binding.chartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardAdminActivity.this, ChartAdminActivity.class));
            }
        });
    }

    private void setUpNotification() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cnt = 0;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    if (("" + ds.child("status").getValue()).equals("Waiting")) {
                        cnt++;
                    }
                }
                if (cnt == 0) {
                    binding.notification.setVisibility(View.GONE);
                } else {
                    binding.notification.setText("" + cnt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


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
                            if (!profileImage.equals("null") && !isDestroyed()) {
                                Glide.with(DashboardAdminActivity.this)
                                        .load(profileImage)
                                        .placeholder(R.drawable.ic_person_gray)
                                        .into(binding.profileTv);
                            }
                            name = "" + snapshot.child("fullName").getValue();
                            if (name.equals("null") || name.equals("")) {
                            } else {
                                Person person = new Person("admin");
                                String newName = person.normalizeName(name);
                                binding.textUserName.setText(newName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
    }
}