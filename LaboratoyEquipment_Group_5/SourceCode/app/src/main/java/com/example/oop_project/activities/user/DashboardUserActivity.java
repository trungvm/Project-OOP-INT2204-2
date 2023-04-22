package com.example.oop_project.activities.user;

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
import com.example.oop_project.databinding.ActivityDashboardUserBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.noties.markwon.Markwon;


public class DashboardUserActivity extends AppCompatActivity {
    boolean isLayoutOpen = false;
    String name = "";
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
        binding.layoutEquipmentBorrowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardUserActivity.this, EquipmentsBorrowedActivity.class));
            }
        });
        if (isUser == true) {
            binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
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
        binding.layoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardUserActivity.this, CartActivity.class));
            }
        });
        binding.layoutSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardUserActivity.this, ScheduleActivity.class));
            }
        });

        binding.layoutRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int heightInPxt = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 930, getResources().getDisplayMetrics());
                if (isLayoutOpen) {
                    binding.contentRulesSv.setVisibility(View.GONE);
                    binding.layoutRules.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    binding.layoutRules.requestLayout();
                    isLayoutOpen = false;
                } else {
                    binding.layoutRules.getLayoutParams().height = heightInPxt;
                    binding.layoutRules.requestLayout();
                    binding.contentRulesSv.setVisibility(View.VISIBLE);
                    isLayoutOpen = true;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Rules");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final Markwon markwon = Markwon.create(DashboardUserActivity.this);
                            String text = "" + snapshot.child("content").getValue();

                            markwon.setMarkdown(binding.contentRules, text);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null && isLoginWithout == 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else if (isLoginWithout == 1) {
            binding.textUserName.setText("Anonymous");
        } else {
            isUser = true;
            String email = firebaseUser.getEmail();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String profileImage = "" + snapshot.child("profileImage").getValue();
                            if (!profileImage.equals("null") && !isDestroyed()) {
                                Glide.with(DashboardUserActivity.this)
                                        .load(profileImage)
                                        .placeholder(R.drawable.ic_person_gray)
                                        .into(binding.profileTv);
                            }
                            name = "" + snapshot.child("fullName").getValue();
                            if (name.equals("null")) {
                                binding.textUserName.setText(email);
                            } else {
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