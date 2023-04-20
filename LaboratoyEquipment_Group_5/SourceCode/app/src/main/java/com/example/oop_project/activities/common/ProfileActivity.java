package com.example.oop_project.activities.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.oop_project.MyApplication;
import com.example.oop_project.R;
import com.example.oop_project.activities.admin.DashboardAdminActivity;
import com.example.oop_project.activities.user.DashboardUserActivity;
import com.example.oop_project.activities.user.FavoriteActivity;
import com.example.oop_project.databinding.ActivityProfileBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseAuth firebaseAuth;
    private int favoriteCount = 0;
    private String role = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInformation();
        int currentTab = getIntent().getIntExtra("CURRENT_TAB", 0);
        binding.bottomNavigationView.getMenu().getItem(currentTab).setChecked(true);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        if(role.equals("user")){
                            Intent intent = new Intent(ProfileActivity.this, DashboardUserActivity.class);
                            intent.putExtra("CURRENT_TAB", 0);
                            startActivity(intent);
                            item.setChecked(true);
                            finish(); // optional, để đóng MainActivity khi chuyển sang HomeActivity
                            return true;
                        }else if(role.equals("admin")){
                            Intent intent = new Intent(ProfileActivity.this, DashboardAdminActivity.class);
                            intent.putExtra("CURRENT_TAB", 0);
                            startActivity(intent);
                            item.setChecked(true);
                            finish(); // optional, để đóng MainActivity khi chuyển sang HomeActivity
                            return true;
                        }
                    case R.id.menuFavorite:
                        Intent intent1 = new Intent(ProfileActivity.this, FavoriteActivity.class);
                        intent1.putExtra("CURRENT_TAB", 1);
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.menuAccount:
                        Intent intent2 = new Intent(ProfileActivity.this, ProfileActivity.class);
                        intent2.putExtra("CURRENT_TAB", 2);
                        startActivity(intent2);
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
        binding.profileEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class));

            }
        });
    }

    private void loadUserInformation() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = "" + snapshot.child("email").getValue();
                        String fullName = "" + snapshot.child("fullName").getValue();
                        String profileImage = "" + snapshot.child("profileImage").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String accountType = "" + snapshot.child("accountType").getValue();
                        String mobile = "" + snapshot.child("mobile").getValue();
                        String address = "" + snapshot.child("address").getValue();
                        String otherInfor = "" + snapshot.child("otherInfor").getValue();
                        String birthday = "" + snapshot.child("birthday").getValue();
                        String gender = "" + snapshot.child("gender").getValue();
                        role = "" + snapshot.child("accountType").getValue();
                        String sex = "N/A";
                        if(gender.equals("1")){
                            sex = "Nam";
                        }else if(gender.equals("2")){
                            sex = "Nữ";
                        }
                        String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                        // set data to ui
                        binding.emailTv.setText(email == ""? "N/A" : email);
                        binding.nameTv.setText(fullName.equals("null")? "N/A" : fullName);
                        binding.memberTv.setText(formattedDate);
                        binding.accountTv.setText(accountType);
                        binding.mobileTv.setText(mobile.equals("null") ? "N/A" : mobile);
                        binding.addressTv.setText(address.equals("null") ? "N/A" :address);
                        binding.otherInfo.setText(otherInfor.equals("null") ? "N/A" : otherInfor);
                        binding.genderTv.setText(sex);
                        if(!profileImage.equals("null") && !isDestroyed()){
                            Glide.with(ProfileActivity.this)
                                    .load(profileImage)
                                    .placeholder(R.drawable.ic_person_gray)
                                    .into(binding.profileTv);
                        }
                        binding.birthdayTv.setText(birthday.equals("null") ? "N/A" : birthday);
                        if(accountType.equals("admin")) {
                            binding.bottomNavigationView.getMenu().findItem(R.id.menuFavorite).setVisible(false);
                        }


                        DatabaseReference refFavorte = FirebaseDatabase.getInstance().getReference("Users");
                        ref.child(firebaseAuth.getUid()).child("Favorites")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                       for(DataSnapshot ds : snapshot.getChildren()){
                                           favoriteCount++;
                                       }
                                        binding.favoriteTv.setText("" + favoriteCount);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}