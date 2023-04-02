package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.oop_project.adapters.AdapterCategory;
import com.example.oop_project.databinding.ActivityEquipmentManagerBinding;
import com.example.oop_project.models.ModelCategory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EquipmentManagerActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    public ActivityEquipmentManagerBinding binding;
    private ArrayList<ModelCategory> categoryArrayList;
    private AdapterCategory adapterCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadCategories();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EquipmentManagerActivity.this, DashboardAdminActivity.class));
                finish();
            }
        });

        // edit text change list
        binding.searchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterCategory.getFilter().filter(s);
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.addEquipmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EquipmentManagerActivity.this, EquipmentAddActivity.class));
                finish();
            }
        });
        // handle click logout
        // click add category
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EquipmentManagerActivity.this, CategoryAddActivity.class));
                finish();
            }
        });
    }

    private void loadCategories() {
        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear all arraylist before, recall
                categoryArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelCategory category = ds.getValue(ModelCategory.class);

                    categoryArrayList.add(category);
                }
                adapterCategory = new AdapterCategory(EquipmentManagerActivity.this, categoryArrayList);
                binding.categories.setAdapter(adapterCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
