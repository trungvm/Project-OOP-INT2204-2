package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.oop_project.adapters.AdapterEquipmentCart;
import com.example.oop_project.adapters.AdapterEquipmentFavorite;
import com.example.oop_project.databinding.ActivityCartBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private ActivityCartBinding binding;
    private ArrayList<ModelEquipment> equipmentArrayList;
    private AdapterEquipmentCart adapterEquipmentCart;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LoadCartEquipments();
    }

    private void LoadCartEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Carts")
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
                        adapterEquipmentCart = new AdapterEquipmentCart(CartActivity.this, equipmentArrayList);
                        binding.favoriteRv.setAdapter(adapterEquipmentCart);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}