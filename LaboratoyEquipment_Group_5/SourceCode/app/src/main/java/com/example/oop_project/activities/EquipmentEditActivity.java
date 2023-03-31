package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.oop_project.databinding.ActivityEquipmentEditBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipmentEditActivity extends AppCompatActivity {
    private ActivityEquipmentEditBinding binding;
    private String equipmentId;
    private ProgressDialog progressDialog;
    private ArrayList<String> categoryTitleArraylist;
    private ArrayList<String> categoryIdArraylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        equipmentId = getIntent().getStringExtra("equipmentId");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please waitl!");
        progressDialog.setCanceledOnTouchOutside(false);
        
        loadCategories();
        loadEquipmentInfo();

        // handle click, pick category
        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

         


    }
    String title = "", description = "", manual = "";
    int quantity = 0;
    private void validateData() {
        title = binding.titleE.getText().toString().trim();
        description = binding.descriptionE.getText().toString().trim();
        manual = binding.manualE.getText().toString().trim();
        String sQuantity = binding.quantityE.getText().toString().trim();
        quantity = Integer.parseInt(sQuantity);

        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(selectedCategoryTitle)){
            Toast.makeText(this, "Chose Category", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(manual)){
            Toast.makeText(this, "Enter Manual", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(sQuantity)){
            Toast.makeText(this, "Enter Quantity", Toast.LENGTH_SHORT).show();
        }else{
            uploadToFirebase();
        }
    }

    private void uploadToFirebase() {
        progressDialog.setMessage("Updating equipment infomation...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", ""+title);
        hashMap.put("description", ""+description);
        hashMap.put("categoryId", ""+selectedCategoryId);
        hashMap.put("manual", ""+manual);
        hashMap.put("quantity", quantity);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(equipmentId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(EquipmentEditActivity.this, "Update successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                        Toast.makeText(EquipmentEditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadEquipmentInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(equipmentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        selectedCategoryId = ""+snapshot.child("categoryId").getValue();
                        String description = "" + snapshot.child("description").getValue();
                        String manual = ""+snapshot.child("manual").getValue();
                        String title = ""+snapshot.child("title").getValue();
                        int quantity =  Integer.parseInt(""+snapshot.child("quantity").getValue());

                        //
                        binding.titleE.setText(title);
                        binding.manualE.setText(manual);
                        binding.quantityE.setText(""+quantity);
                        binding.descriptionE.setText(description);

                        DatabaseReference refEquipmentCategory = FirebaseDatabase.getInstance().getReference("Categories");
                        refEquipmentCategory.child(selectedCategoryId)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String category =""+snapshot.child("title").getValue();
                                        binding.categoryTv.setText(category);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String selectedCategoryId = "", selectedCategoryTitle = "";
    private void categoryDialog(){
        String[] categoriesArray = new String[categoryTitleArraylist.size()];
        for(int i = 0; i < categoryTitleArraylist.size(); i++){
            categoriesArray[i] = categoryTitleArraylist.get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Category!")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCategoryId = categoryIdArraylist.get(which);
                        selectedCategoryTitle = categoryTitleArraylist.get(which);

                        binding.categoryTv.setText(selectedCategoryTitle);
                    }
                }).show();
    }
    private void loadCategories() {
        categoryTitleArraylist = new ArrayList<>();
        categoryIdArraylist = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArraylist.clear();
                categoryIdArraylist.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String id = ""+ds.child("id").getValue();
                    String category = ""+ds.child("title").getValue();
                    categoryIdArraylist.add(id);
                    categoryTitleArraylist.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}