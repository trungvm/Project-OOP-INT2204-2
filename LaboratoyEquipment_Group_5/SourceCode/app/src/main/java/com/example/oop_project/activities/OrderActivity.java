package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.oop_project.databinding.ActivityOrderBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity {
    private com.example.oop_project.databinding.ActivityOrderBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> listOfKey;
    private ProgressDialog progressDialog;
    private ArrayList<String> listOfEquipmentId;
    private ArrayList<String> listOfTitleEquipment;
    @Override
    protected void onDestroy(){
        super.onDestroy();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if((""+ds.child("status").getValue()).equals("new")){
                                ds.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    protected void onStop() {
        super.onStop();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if((""+ds.child("status").getValue()).equals("new")){
                                ds.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    protected void onPause() {

        super.onPause();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if((""+ds.child("status").getValue()).equals("new")){
                                ds.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait!");
        progressDialog.setCanceledOnTouchOutside(false);
        listOfKey = getIntent().getStringArrayListExtra("listOfKey");
        listOfEquipmentId = getIntent().getStringArrayListExtra("listOfEquipmentId");
        listOfTitleEquipment = getIntent().getStringArrayListExtra("listOfTitleEquipment");
        loadInformation();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                ref.child(firebaseAuth.getUid())
                        .child("Borrowed")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for(DataSnapshot ds: snapshot.getChildren()){
                                            if((""+ds.child("status").getValue()).equals("new")){
                                                ds.getRef().removeValue();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                startActivity(new Intent(OrderActivity.this, CartActivity.class));
                finish();
            }
        });
        binding.birthdayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        OrderActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dateOfBirth = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                binding.birthdayTv.setText(dateOfBirth);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        binding.genderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuGender();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    String fullName, email, mobile, address, birthday, gender, otherInfor = "", report = "";
    private void validateData() {
        fullName = binding.fullNameTv.getText().toString().trim();
        email = binding.emailTv.getText().toString().trim();
        mobile = binding.mobileTv.getText().toString().trim();
        address = binding.addressTv.getText().toString().trim();
        birthday = binding.birthdayTv.getText().toString().trim();
        gender = binding.genderTv.getText().toString().trim();
        otherInfor = binding.otherInfo.getText().toString().trim();
        report = binding.reportEt.getText().toString().trim();

        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Điền họ và tên!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Điền email!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mobile)){
            Toast.makeText(this, "Điền số điện thoại!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(address)){
            Toast.makeText(this, "Điền địa chỉ!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(gender)){
            Toast.makeText(this, "Điền giới tính", Toast.LENGTH_SHORT).show();
        }else{
            insertData();
        }

    }
    String profileImage;
    private void insertData() {
        progressDialog.setMessage("Đang tiến hành mượn!");
        progressDialog.show();
        DatabaseReference refBorowed = FirebaseDatabase.getInstance().getReference("Users");
        refBorowed.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                           if((ds.child("status").getValue()).equals("new")){
                               DatabaseReference reference =  ds.child("status").getRef();
                               reference.setValue("Borrowed");
                               int quantityBorrowed = Integer.parseInt(""+ds.child("quantityBorrowed").getValue());
                               String equipmentId = "" + ds.child("equipmentId").getValue();
                               DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Equipments");
                               reference1.child(equipmentId)
                                       .addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               int quantity = Integer.parseInt("" + snapshot.child("quantity").getValue());
                                               int x = quantity-quantityBorrowed;
                                               DatabaseReference ref2 = snapshot.child("quantity").getRef();
                                               ref2.setValue(x);
                                               int numberOfBorrowings;
                                               if (snapshot.hasChild("numberOfBorrowings")) {
                                                   // Nếu tồn tại, lấy giá trị của child "numberOfBorrowings"
                                                   numberOfBorrowings = Integer.parseInt(snapshot.child("numberOfBorrowings").getValue().toString());
                                               } else {
                                                   // Nếu không tồn tại, thêm mới thuộc tính "numberOfBorrowings" và set giá trị là 0
                                                   snapshot.getRef().child("numberOfBorrowings").setValue(0);
                                                   numberOfBorrowings = 0;
                                               }
                                               numberOfBorrowings++;
                                               snapshot.getRef().child("numberOfBorrowings").setValue(numberOfBorrowings);
                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });
                           }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        long timestamp = System.currentTimeMillis();
        for(int i = 0; i < listOfKey.size(); i++){
            HashMap <String, Object> hashMap = new HashMap<>();
            hashMap.put("fullName", fullName);
            hashMap.put("email", email);
            hashMap.put("mobile", mobile);
            hashMap.put("address", address);
            hashMap.put("birthday", birthday);
            hashMap.put("gender", gender);
            hashMap.put("otherInfor", otherInfor);
            hashMap.put("report", report);
            hashMap.put("timestamp", timestamp);
            hashMap.put("status", "Borrowed");
            hashMap.put("uid", firebaseAuth.getUid());
            hashMap.put("equipmentId", listOfEquipmentId.get(i));
            hashMap.put("title", listOfTitleEquipment.get(i));

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
            ref.child(listOfKey.get(i))
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(OrderActivity.this, "Mượn thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OrderActivity.this, CartActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(OrderActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                        }
                    });

        }



    }

    private void showMenuGender() {
        PopupMenu popupMenu = new PopupMenu(this, binding.genderTv);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Nam");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Nữ");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // get id of item clicked
                int which = item.getItemId();
                if(which == 0){
                    binding.genderTv.setText("Nam");
                }else if(which == 1){
                    binding.genderTv.setText("Nữ");
                }
                return false;
            }
        });
    }

    String quantityBorrowed = "";
    private void loadInformation() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String fullName = "" + snapshot.child("fullName").getValue();
                        String email = "" + snapshot.child("email").getValue();
                        String mobile = "" + snapshot.child("mobile").getValue();
                        String address = "" + snapshot.child("address").getValue();
                        String birthday = "" + snapshot.child("birthday").getValue();
                        String gender = "" + snapshot.child("gender").getValue();
                        String otherInfor = "" + snapshot.child("otherInfor").getValue();
                        profileImage = "" + snapshot.child("equipmentImage").getValue();

                        binding.fullNameTv.setText(fullName.equals("null") ? "" : fullName);
                        binding.emailTv.setText(fullName.equals("nulll") ? "" : email);
                        binding.mobileTv.setText(mobile.equals("null") ? "" : mobile);
                        binding.addressTv.setText(address.equals("null") ? "" : address);
                        binding.birthdayTv.setText(birthday.equals("null") ? "" : birthday);
                        binding.otherInfo.setText(otherInfor.equals("null") ? "" : otherInfor);
                        if(gender.equals("1")){
                            binding.genderTv.setText("Nam");
                        }else if(gender.equals("2")){
                            binding.genderTv.setText("Nữ");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshots) {

                        for(DataSnapshot ds : snapshots.getChildren()){
                           if((ds.child("status").getValue()).equals("new")){
                               String part = "" + ds.child("quantityBorrowed").getValue();
                               quantityBorrowed += "x" +part + "\n";
                               String equipmentId = "" + ds.child("equipmentId").getValue();
                               DatabaseReference refChild = FirebaseDatabase.getInstance().getReference("Equipments");
                               refChild.child(equipmentId)
                                       .addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot snapshot) {
                                               String name = "" + snapshot.child("title").getValue();
                                               binding.textBorrowed.append(name + "\n");
                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError error) {

                                           }
                                       });
                           }
                        }
                        binding.quantityBorrowed.setText(quantityBorrowed);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}