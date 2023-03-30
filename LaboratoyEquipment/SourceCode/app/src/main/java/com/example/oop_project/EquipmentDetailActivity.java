
package com.example.oop_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.oop_project.databinding.ActivityEquipmentDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EquipmentDetailActivity extends AppCompatActivity {
    private ActivityEquipmentDetailBinding binding;
    String equipmentid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        equipmentid = intent.getStringExtra("equipmentId");

        loadEquipmentDetails();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }

    private void loadEquipmentDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(equipmentid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String title = ""+snapshot.child("title").getValue();
                        String description = ""+snapshot.child("description").getValue();
                        String timestamp = ""+snapshot.child("timestamp").getValue();
                        String quantity = ""+snapshot.child("quantity").getValue();
                        String manual = ""+snapshot.child("manual").getValue();
                        String categoryId = ""+snapshot.child("categoryId").getValue();

                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                        DatabaseReference refCategory = FirebaseDatabase.getInstance().getReference("Categories");
                        refCategory.child(categoryId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String title = ""+snapshot.child("title").getValue();
                                                binding.categoryTv.setText(title);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                        binding.titleTv.setText(title);
                        binding.dateTv.setText(date);
                        binding.quantityTv.setText(quantity);
                        binding.descriptionTv.setText(description);
                        binding.manualTv.setText(manual);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}