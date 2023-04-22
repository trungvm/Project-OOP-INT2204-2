package com.example.oop_project.activities.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.MyApplication;
import com.example.oop_project.R;
import com.example.oop_project.activities.admin.DashboardAdminActivity;
import com.example.oop_project.databinding.ActivityEquipmentDetailBinding;
import com.example.oop_project.models.ModelCategory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.noties.markwon.Markwon;

public class EquipmentDetailActivity extends AppCompatActivity {
    public FirebaseAuth firebaseAuth;
    String equipmentId;
    boolean isInMyFavorite = false;
    String status = "";
    String key = "";
    String role = "";
    String personI4 = "";
    String preStatus = "";
    private ActivityEquipmentDetailBinding binding;
    private String quantity = "";
    private boolean isCallbackHandled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        equipmentId = intent.getStringExtra("equipmentId");
        if (intent.getStringExtra("status") != null) {
            status = intent.getStringExtra("status");
        }
        if (intent.getStringExtra("key") != null) {
            key = intent.getStringExtra("key");
        }
        if (intent.getStringExtra("role") != null) {
            role = intent.getStringExtra("role");
        }
        if (intent.getStringExtra("personI4") != null) {
            personI4 = intent.getStringExtra("personI4");
        }
        if (intent.getStringExtra("preStatus") != null) {
            preStatus = intent.getStringExtra("preStatus");
        }
        firebaseAuth = FirebaseAuth.getInstance();
        if (!role.equals("admin") && firebaseAuth.getCurrentUser() != null) {
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

        if (role.equals("admin")) {
            binding.favoriteBtn.setVisibility(View.GONE);
            binding.addCartBtn.setVisibility(View.GONE);
        }
    }

    private void checkEquipmentQuantity() {
        int quantityInStock = Integer.parseInt(quantity);
        if (quantityInStock == 0) {
            Toast.makeText(this, "Thiết bị đã bị mượn hết", Toast.LENGTH_SHORT).show();
        } else {
            // Thêm sản phẩm vào trong cart;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).child("Carts").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(equipmentId)) {
                        Toast.makeText(EquipmentDetailActivity.this, "Đã có thiết bị trong giỏ hàng!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        MyApplication.addToCart(EquipmentDetailActivity.this, equipmentId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void loadEquipmentDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(equipmentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String title = "" + snapshot.child("title").getValue();
                        String description = "" + snapshot.child("description").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        quantity = "" + snapshot.child("quantity").getValue();
                        String manual = "" + snapshot.child("manual").getValue();
                        manual = manual.replace("\\n", "\n");
                        String newManual = manual;
                        String categoryId = "" + snapshot.child("categoryId").getValue();
                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                        String viewed = "" + snapshot.child("viewed").getValue();
                        String equipmentImage = "" + snapshot.child("equipmentImage").getValue();
                        String numberOfBorrowings = "0";
                        if (snapshot.hasChild("numberOfBorrowings")) {
                            numberOfBorrowings = "" + snapshot.child("numberOfBorrowings").getValue();
                        }
                        ModelCategory modelCategory = new ModelCategory();
                        modelCategory.getDataFromFireBase(categoryId).addOnCompleteListener(new OnCompleteListener<ModelCategory>() {
                            @Override
                            public void onComplete(@NonNull Task<ModelCategory> task) {
                                if (task.isSuccessful()) {
                                    ModelCategory newModelCategory = task.getResult();
                                    binding.categoryTv.setText(newModelCategory.getTitle());
                                    binding.position.setText(newModelCategory.getPosition());
                                }
                            }
                        });
                        if (!personI4.equals("admin")) {
                            int viewede = Integer.parseInt(viewed) + 1;
                            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Equipments");
                            ref1.child(equipmentId).child("viewed")
                                    .setValue(viewede)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }
                        binding.titleTv.setText(title);
                        binding.dateTv.setText(date);
                        binding.numberOfBorrowings.setText(numberOfBorrowings);
                        binding.quantityTv.setText(quantity);
                        if (status.equals("Borrowed") && personI4.equals("admin") && preStatus.equals("Waiting")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestampAdminSchedule").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String fullName = "" + snapshot.child("fullName").getValue();
                                            String email = "" + snapshot.child("email").getValue();
                                            String address = "" + snapshot.child("address").getValue();
                                            String birthday = "" + snapshot.child("birthday").getValue();
                                            String mobile = "" + snapshot.child("mobile").getValue();
                                            String gender = "" + snapshot.child("gender").getValue();
                                            String otherInfor = "" + snapshot.child("otherInfor").getValue();
                                            String startDate = "" + snapshot.child("startDate").getValue();
                                            String endDate = "" + snapshot.child("endDate").getValue();
                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            binding.descriptionTv.setText("Thời gian xác nhận : " + date
                                                    + "\n" + "Thời gian dự kiến mượn : " + startDate
                                                    + "\n" + "Thời gian dự kiến trả : " + endDate
                                            );
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " + email
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
                        } else if (status.equals("History") && personI4.equals("admin") && preStatus.equals("Waiting")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestampAdminSchedule").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String fullName = "" + snapshot.child("fullName").getValue();
                                            String email = "" + snapshot.child("email").getValue();
                                            String address = "" + snapshot.child("address").getValue();
                                            String birthday = "" + snapshot.child("birthday").getValue();
                                            String mobile = "" + snapshot.child("mobile").getValue();
                                            String gender = "" + snapshot.child("gender").getValue();
                                            String otherInfor = "" + snapshot.child("otherInfor").getValue();
                                            String startDate = "" + snapshot.child("startDate").getValue();
                                            String endDate = "" + snapshot.child("endDate").getValue();
                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            String reportHistory = "" + snapshot.child("reportHistory").getValue();
                                            binding.descriptionTv.setText("Thời gian xác nhận : " + date
                                                    + "\n" + "Thời gian dự kiến mượn : " + startDate
                                                    + "\n" + "Thời gian dự kiến trả : " + endDate
                                            );
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Báo cáo về thiết bị lúc trả : " + reportHistory
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " + email
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
                        } else if (status.equals("Refuse") && personI4.equals("admin")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestampAdminSchedule").getValue();
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
                                            binding.descriptionTv.setText("Thời gian xác nhận : " + date
                                                    + "\n" + "Thời gian dự kiến mượn : " + startDate
                                                    + "\n" + "Thời gian dự kiến trả : " + endDate
                                                    + "\n" + "Lí do hủy : " + reportRefuse
                                            );
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " + email
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
                        } else if ((status.equals("Borrowed") && personI4.equals("admin")) || (status.equals("Waiting") && personI4.equals("admin"))) {
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
                                            binding.descriptionTv.setText("Thời gian mượn : " + date);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " + email
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
                        } else if (status.equals("History") && personI4.equals("admin")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String timestampReturn = "" + snapshot.child("timestampReturn").getValue();
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
                                            if (!timestamp.equals("null") && !timestampReturn.equals("null")) {
                                                date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                                dateReturn = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestampReturn));
                                            }
                                            binding.descriptionTv.setText("Thời gian mượn: " + date + " - Thời gian trả : " + dateReturn);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Báo cáo về thiết bị lúc trả: " + (reportHistory.equals("null") ? "" : reportHistory)
                                                    + "\n" + "Họ và tên: " + fullName
                                                    + "\n" + "Email: " + email
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

                        } else if (status.equals("Borrowed") && preStatus.equals("Waiting")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String timestampAdminSchedule = "" + snapshot.child("timestampAdminSchedule").getValue();
                                            String dateSchedule = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestampAdminSchedule));
                                            String startDate = "" + snapshot.child("startDate").getValue();
                                            String endDate = "" + snapshot.child("endDate").getValue();

                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            binding.descriptionTv.setText("Thời gian mượn : " + date
                                                    + "\n" + "Thời gian xác nhận : " + dateSchedule
                                                    + "\n" + "Thời gian dự kiến mượn : " + startDate
                                                    + "\n" + "Thời gian dự kiến trả : " + endDate
                                            );
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        } else if (status.equals("History") && preStatus.equals("Waiting")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String timestampAdminSchedule = "" + snapshot.child("timestampAdminSchedule").getValue();
                                            String dateSchedule = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestampAdminSchedule));
                                            String startDate = "" + snapshot.child("startDate").getValue();
                                            String endDate = "" + snapshot.child("endDate").getValue();
                                            String timeReturn = "" + snapshot.child("timestampReturn").getValue();
                                            String dateReturn = MyApplication.formatTimestampToDetailTime(Long.parseLong(timeReturn));
                                            String reportHistory = "" + snapshot.child("reportHistory").getValue();
                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            binding.descriptionTv.setText("Thời gian mượn : " + date
                                                    + "\n" + "Thời gian xác nhận : " + dateSchedule
                                                    + "\n" + "Thời gian dự kiến mượn : " + startDate
                                                    + "\n" + "Thời gian dự kiến trả : " + endDate
                                                    + "\n" + "Thời gian trả : " + dateReturn

                                            );
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Báo cáo về thiết bị lúc trả : " + reportHistory
                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        } else if (status.equals("Borrowed")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String report = "" + snapshot.child("report").getValue();

                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            binding.descriptionTv.setText("Thời gian mượn : " + date);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        } else if (status.equals("History")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestamp").getValue();
                                            String timestampReturn = "" + snapshot.child("timestampReturn").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String reportHistory = "" + snapshot.child("reportHistory").getValue();
                                            String date = "";
                                            String dateReturn = "";
                                            if (!timestamp.equals("null") && !timestampReturn.equals("null")) {
                                                date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                                dateReturn = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestampReturn));
                                            }
                                            binding.descriptionTv.setText("Thời gian mượn: " + date + " - Thời gian trả : " + dateReturn);
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report)
                                                    + "\n" + "Báo cáo về thiết bị lúc trả : " + reportHistory
                                            );
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        } else if (status.equals("Waiting")) {
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

                        } else if (status.equals("Refuse")) {
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                            ref.child(key)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String timestamp = "" + snapshot.child("timestampAdminSchedule").getValue();
                                            String report = "" + snapshot.child("report").getValue();
                                            String reportRefuse = "" + snapshot.child("reportRefuse").getValue();
                                            String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                                            String startDate = "" + snapshot.child("startDate").getValue();
                                            String endDate = "" + snapshot.child("endDate").getValue();
                                            binding.descriptionTv.setText("Thời gian xác nhận : " + date
                                                    + "\n" + "Thời gian dự kiến mượn : " + startDate
                                                    + "\n" + "Thời gian dự kiến trả : " + endDate
                                                    + "\n" + "Lí do hủy : " + reportRefuse
                                            );
                                            binding.manualTv.setText("Báo cáo về thiết bị lúc mượn: " + (report.equals("null") ? "" : report));
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        } else {
                            binding.descriptionTv.setText(description);
                            final Markwon markwon = Markwon.create(EquipmentDetailActivity.this);

                            markwon.setMarkdown(binding.manualTv, newManual);
                        }
                        binding.viewedTv.setText(viewed);
                        Handler handler = new Handler(Looper.getMainLooper());
                        if(!isDestroyed()){
                            Glide.with(EquipmentDetailActivity.this)
                                    .load(equipmentImage)
                                    .centerCrop()
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            if (!isCallbackHandled) { // Kiểm tra nếu chưa xử lý callback
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        binding.progressBar.setVisibility(View.VISIBLE);
                                                        binding.imageView.setVisibility(View.GONE);
                                                    }
                                                });

                                                // Đánh dấu là đã xử lý callback
                                                isCallbackHandled = true;
                                            }

                                            // Trả về false để cho phép Glide xử lý callback tiếp
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            if (!isCallbackHandled) { // Kiểm tra nếu chưa xử lý callback
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        binding.imageView.setVisibility(View.VISIBLE);
                                                        binding.progressBar.setVisibility(View.GONE);
                                                    }
                                                });

                                                // Đánh dấu là đã xử lý callback
                                                isCallbackHandled = true;
                                            }

                                            // Trả về false để cho phép Glide xử lý callback tiếp
                                            return false;
                                        }
                                    })
                                    .into(binding.imageView);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(EquipmentDetailActivity.this, "You're not logged in", Toast.LENGTH_SHORT).show();
                } else {
                    if (isInMyFavorite) {
                        MyApplication.removeFromFavorite(EquipmentDetailActivity.this, equipmentId);
                    } else {
                        MyApplication.addToFavorite(EquipmentDetailActivity.this, equipmentId);
                    }
                }
            }
        });
    }

    private void checkIsFavorite() {
        if (firebaseAuth.getUid() != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
            ref.child(firebaseAuth.getUid()).child("Favorites").child(equipmentId)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            isInMyFavorite = snapshot.exists();
                            if (isInMyFavorite) {
                                binding.favoriteBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_favorite_white, 0, 0);
                                binding.favoriteBtn.setText("Remove Favorite");
                            } else {
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