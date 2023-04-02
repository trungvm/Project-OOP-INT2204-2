package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.oop_project.R;
import com.example.oop_project.adapters.AdapterEquipmentFavorite;
import com.example.oop_project.databinding.ActivityFavoriteBinding;
import com.example.oop_project.databinding.ActivityProfileBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private ArrayList<ModelEquipment> equipmentArrayList;
    private AdapterEquipmentFavorite adapterEquipmentFavorite;
    private FirebaseAuth  firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        int currentTab = getIntent().getIntExtra("CURRENT_TAB", 0);
        binding.bottomNavigationView.getMenu().getItem(currentTab).setChecked(true);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuHome:
                        Intent intent = new Intent(FavoriteActivity.this, DashboardUserActivity.class);
                        intent.putExtra("CURRENT_TAB", 0);
                        startActivity(intent);
                        finish(); //
                        return true;
                    case R.id.menuFavorite:
                        Intent intent1 = new Intent(FavoriteActivity.this, FavoriteActivity.class);
                        intent1.putExtra("CURRENT_TAB", 1);
                        startActivity(intent1);
                        finish();
                        return true;
                    case R.id.menuAccount:
                        Intent intent2 = new Intent(FavoriteActivity.this, ProfileActivity.class);
                        intent2.putExtra("CURRENT_TAB", 2);
                        startActivity(intent2);
                        item.setChecked(true);
                        finish(); //
                        return true;
                }
                return false;
            }
        });
        loadFavoriteEquipments();
    }

    private void loadFavoriteEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Favorites")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String equipmentId = ""+ds.child("equipmentId").getValue();
                            ModelEquipment model = new ModelEquipment();
                            model.setId(equipmentId);
                            equipmentArrayList.add(model);
                        }
                        adapterEquipmentFavorite = new AdapterEquipmentFavorite(FavoriteActivity.this, equipmentArrayList);
                        binding.favoriteRv.setAdapter(adapterEquipmentFavorite);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}