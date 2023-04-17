
package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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
    private String quantity = "";
    String status = "";
    String key = "";
    String role = "";
    String personI4 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        equipmentId = intent.getStringExtra("equipmentId");
        if(intent.getStringExtra("status") != null){
            status = intent.getStringExtra("status");
        }
        if(intent.getStringExtra("key") != null){
            key = intent.getStringExtra("key");
        }
        if(intent.getStringExtra("role") != null){
            role = intent.getStringExtra("role");
        }
        if(intent.getStringExtra("personI4") != null){
            personI4 = intent.getStringExtra("personI4");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        if(!role.equals("admin") && firebaseAuth.getCurrentUser() != null){
            checkIsFavorite();
        }

        loadEquipmentDetails();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEquipmentQuantity();
            }
        });

        if(role.equals("admin")){
            binding.favoriteBtn.setVisibility(View.GONE);
            binding.addCartBtn.setVisibility(View.GONE);
        }
    }

    private void checkEquipmentQuantity() {
        int quantityInStock = Integer.parseInt(quantity);
        if(quantityInStock == 0){
            Toast.makeText(this, "Thiết bị đã bị mượn hết", Toast.LENGTH_SHORT).show();
        }else{
            // Thêm sản phẩm vào trong cart;
            MyApplication.addToCart(EquipmentDetailActivity.this, equipmentId);

        }
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
                        quantity = ""+snapshot.child("quantity").getValue();
                        String manual = ""+snapshot.child("manual").getValue();
                        String categoryId = ""+snapshot.child("categoryId").getValue();
                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                        String viewed = ""+snapshot.child("viewed").getValue();
                        String equipmentImage = "" + snapshot.child("equipmentImage").getValue();
                        String numberOfBorrowings = "0";
                        if(snapshot.hasChild("numberOfBorrowings")){
                            numberOfBorrowings = "" + snapshot.child("numberOfBorrowings").getValue();
                        }
                        DatabaseReference refCategory = FirebaseDatabase.getInstance().getReference("Categories");
                        refCategory.child(categoryId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String title = ""+snapshot.child("title").getValue();
                                                String position = "" + snapshot.child("position").getValue();
                                                if(position.equals("null")){

                                                }else{
                                                    binding.position.setText(position);
                                                }
                                                binding.categoryTv.setText(title);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                        binding.titleTv.setText(title);
                        binding.dateTv.setText(date);
                        binding.numberOfBorrowings.setText(numberOfBorrowings);
                        binding.quantityTv.setText(quantity);
                        if(status.equals("Refuse") && personI4.equals("admin")){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String fullName = "" + snapshot.child("fullName").getValue();
                                            String email = "" + snapshot.child("email").getValue();
                                            String address = "" + snapshot.child("address").getValue();
                                            String birthday = "" + snapshot.child("birthday").getValue();
                                            String mobile = "" + snapshot.child("mobile").getValue();
                                            String gender = "" + snapshot.child("gender").getValue();
                                            String otherInfor = "" + snapshot.child("otherInfor").getValue();
                                            String reportRefuse = "" + snapshot.child("reportRefuse").getValue();
                                            String startDate = "" + snapshot.child("startDate").getValue();
                                            String endDate = "" + snapshot.child("endDate").getValue();
                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            binding.descriptionTv.setText("Thời gian xác nhận : " +  date
                                            +"\n" + "Thời gian dự kiến mượn : " + startDate
                                            +"\n" + "Thời gian dự kiến trả : " + endDate
                                            +"\n" + "Lí do hủy : " + reportRefuse
                                            );
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " +email
                                                    + "\n" + "Địa chỉ: " + address
                                                    + "\n" + "Ngày sinh: " + birthday
                                                    + "\n" + "Số điện thoại: " + mobile
                                                    + "\n" + "Giới tính: " + gender
                                                    + "\n" + "Thông tin khác: " + otherInfor
                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else if((status.equals("Borrowed") && personI4.equals("admin") )||(status.equals("Waiting") && personI4.equals("admin")) ){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String fullName = "" + snapshot.child("fullName").getValue();
                                            String email = "" + snapshot.child("email").getValue();
                                            String address = "" + snapshot.child("address").getValue();
                                            String birthday = "" + snapshot.child("birthday").getValue();
                                            String mobile = "" + snapshot.child("mobile").getValue();
                                            String gender = "" + snapshot.child("gender").getValue();
                                            String otherInfor = "" + snapshot.child("otherInfor").getValue();

                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            binding.descriptionTv.setText("Thời gian mượn : " +  date);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " +email
                                                    + "\n" + "Địa chỉ: " + address
                                                    + "\n" + "Ngày sinh: " + birthday
                                                    + "\n" + "Số điện thoại: " + mobile
                                                    + "\n" + "Giới tính: " + gender
                                                    + "\n" + "Thông tin khác: " + otherInfor
                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else if(status.equals("History") && personI4.equals("admin")){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String timestampReturn = ""+ snapshot.child("timestampReturn").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String reportHistory = "" + snapshot.child("reportHistory").getValue();
                                            String date = "";
                                            String dateReturn = "";

                                            String fullName = "" + snapshot.child("fullName").getValue();
                                            String email = "" + snapshot.child("email").getValue();
                                            String address = "" + snapshot.child("address").getValue();
                                            String birthday = "" + snapshot.child("birthday").getValue();
                                            String mobile = "" + snapshot.child("mobile").getValue();
                                            String gender = "" + snapshot.child("gender").getValue();
                                            String otherInfor = "" + snapshot.child("otherInfor").getValue();
                                            if(!timestamp.equals("null") && !timestampReturn.equals("null")){
                                                date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                                dateReturn = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestampReturn));
                                            }
                                            binding.descriptionTv.setText("Thời gian mượn: " +  date + " - Thời gian trả : " + dateReturn);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Báo cáo về thiết bị lúc trả: " + (reportHistory.equals("null") ? "" : reportHistory)
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " +email
                                                    + "\n" + "Địa chỉ: " + address
                                                    + "\n" + "Ngày sinh: " + birthday
                                                    + "\n" + "Số điện thoại: " + mobile
                                                    + "\n" + "Giới tính: " + gender
                                                    + "\n" + "Thông tin khác: " + otherInfor

                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }
                        else if(status.equals("Borrowed")){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String report = "" + snapshot.child("report").getValue();

                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            binding.descriptionTv.setText("Thời gian mượn : " +  date);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else if(status.equals("History")){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String timestampReturn = ""+ snapshot.child("timestampReturn").getValue();

                                            String report = "" + snapshot.child("reportHistory").getValue();
                                            String date = "";
                                            String dateReturn = "";
                                            if(!timestamp.equals("null") && !timestampReturn.equals("null")){
                                                date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                                dateReturn = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestampReturn));
                                            }
                                            binding.descriptionTv.setText("Thời gian mượn: " +  date + " - Thời gian trả : " + dateReturn);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc trả: " + (report.equals("null") ? "" : report));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }else if(status.equals("Waiting")){
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String report = "" + snapshot.child("report").getValue();

                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            String startDate = "" + snapshot.child("startDate").getValue();
                                            String endDate = "" + snapshot.child("endDate").getValue();
                                            binding.descriptionTv.setText("Thời gian dự kiến mượn : " + startDate + " - "
                                                    + "Thời gian dự kiến trả : " + endDate);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        }

                        else{
                            binding.descriptionTv.setText(description);
                            binding.manualTv.setText(manual);
                        }
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