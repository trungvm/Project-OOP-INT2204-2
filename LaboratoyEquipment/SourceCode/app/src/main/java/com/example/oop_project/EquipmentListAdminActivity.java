package com.example.oop_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.example.oop_project.adapters.AdapterEquipmentAdmin;
import com.example.oop_project.databinding.ActivityEquipmentListAdminBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EquipmentListAdminActivity extends AppCompatActivity {
    private ActivityEquipmentListAdminBinding binding;
    private ArrayList<ModelEquipment> equipmentArrayList;
    private AdapterEquipmentAdmin adapterEquipmentAdmin;
    private String categoryId, categoryTitle;

    private static final String TAG = "EQUIPMENT_LIST_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentListAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get data from intent
        Intent intent = getIntent();
        categoryId = intent.getStringExtra("categoryId");
        categoryTitle = intent.getStringExtra("categoryTitle");

        binding.subtitleTv.setText(categoryTitle);
        loadEquipmentList();


        // search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    adapterEquipmentAdmin.getFilter().filter(s);
                }catch (Exception e){
                    Log.d(TAG, "ontextchange:" + e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadEquipmentList() {
        equipmentArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayList.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            ModelEquipment model = ds.getValue(ModelEquipment.class);
                            equipmentArrayList.add(model);
                        }
                        adapterEquipmentAdmin = new AdapterEquipmentAdmin(EquipmentListAdminActivity.this, equipmentArrayList);
                        binding.equipmentRv.setAdapter(adapterEquipmentAdmin);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}