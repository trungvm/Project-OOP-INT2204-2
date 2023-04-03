
package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.MyApplication;
import com.example.oop_project.R;
import com.example.oop_project.databinding.ActivityEquipmentDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EquipmentDetailActivity extends AppCompatActivity {
    private ActivityEquipmentDetailBinding binding;
    public FirebaseAuth firebaseAuth;
    String equipmentId;
    boolean isInMyFavorite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        equipmentId = intent.getStringExtra("equipmentId");

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            checkIsFavorite();
        }

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
        ref.child(equipmentId)
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
                        String viewed = ""+snapshot.child("viewed").getValue();
                        String equipmentImage = "" + snapshot.child("equipmentImage").getValue();
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
                        binding.viewedTv.setText(viewed);
                        Glide.with(EquipmentDetailActivity.this)
                                .load(equipmentImage)
                                .centerCrop()
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        binding.progressBar.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                        binding.imageView.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                })
                                .into(binding.imageView);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firebaseAuth.getCurrentUser() == null){
                    Toast.makeText(EquipmentDetailActivity.this, "You're not logged in", Toast.LENGTH_SHORT).show();
                }else{
                    if(isInMyFavorite){
                        MyApplication.removeFromFavorite(EquipmentDetailActivity.this, equipmentId);
                    }else{
                        MyApplication.addToFavorite(EquipmentDetailActivity.this, equipmentId);
                    }
                }
            }
        });
    }
    private void checkIsFavorite(){
        if(firebaseAuth.getUid() != null){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).child("Favorites").child(equipmentId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isInMyFavorite = snapshot.exists();
                            if(isInMyFavorite){
                                binding.favoriteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_white, 0, 0);
                                binding.favoriteBtn.setText("Remove Favorite");
                            }else{
                                binding.favoriteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_border, 0, 0);
                                binding.favoriteBtn.setText("Add Favorite");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }
}