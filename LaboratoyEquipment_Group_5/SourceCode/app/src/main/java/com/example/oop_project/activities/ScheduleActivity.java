package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.oop_project.adapters.AdapterEquipmentCart;
import com.example.oop_project.adapters.AdapterSchedule;
import com.example.oop_project.databinding.ActivityScheduleBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    private ActivityScheduleBinding binding;
    private ArrayList<ModelEquipment> equipmentArrayList;
    private AdapterSchedule adapterSchedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleActivity.this, DashboardUserActivity.class));
                finish();
            }
        });
        loadListEquipments();
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterSchedule.getFilter().filter(s);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void loadListEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    String title = "" + ds.child("title").getValue();
                    String categoryId = "" + ds.child("categoryId").getValue();
                    String equipmentId = "" + ds.child("id").getValue();
                    String timestamp = "" + ds.child("timestamp").getValue();
                    String manual = "" + ds.child("manual").getValue();
                    String description = "" + ds.child("description").getValue();
                    String quantity = "" + ds.child("quantity").getValue();
                    String equipmentImage = "" + ds.child("equipmentImage").getValue();
                    ModelEquipment model = new ModelEquipment();

                    model.setTitle(title);
                    model.setEquipmentImage(equipmentImage);
                    model.setCategoryId(categoryId);
                    model.setId(equipmentId);
                    model.setTimestamp(Long.parseLong(timestamp));
                    model.setDescription(description);
                    model.setManual(manual);
                    model.setQuantity(Integer.parseInt(quantity));
                    equipmentArrayList.add(model);
                }
                adapterSchedule = new AdapterSchedule(ScheduleActivity.this, equipmentArrayList);
                binding.equipmentRv.setAdapter(adapterSchedule);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}