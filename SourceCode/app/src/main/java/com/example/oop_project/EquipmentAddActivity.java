package com.example.oop_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.oop_project.databinding.ActivityEquipmentAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipmentAddActivity extends AppCompatActivity {
    private ActivityEquipmentAddBinding binding;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "ADD_IMAGE_TAG";
    private ArrayList<String> categoryTitleArrayList, categoryIdArraylist;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        loadEquipmentCategory();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait!");
        progressDialog.setCanceledOnTouchOutside(false);




        // handlo click, go to previous
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(EquipmentAddActivity.this, DashboardAdminActivity.class));
                finish();

            }
        });
        binding.attachImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickIntent();
            }
        });

        binding.categoryTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryPickDialog();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }
    String title = "", description = "",manual = "";
    int quantity = 0;
    private void validateData() {
        title = binding.titleE.getText().toString().trim();
        description = binding.descriptionE.getText().toString().trim();
        quantity = Integer.parseInt(binding.quantityE.getText().toString().trim());
        String quantityStr = binding.quantityE.getText().toString().trim();
        manual = binding.manualE.getText().toString().trim();
        if(TextUtils.isEmpty(title)){
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(description)){
            Toast.makeText(this, "Enter Description", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(selectedCategoryTitle)){
            Toast.makeText(this, "Chose Category", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(manual)){
            Toast.makeText(this, "Enter Manual", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(quantityStr)){
            Toast.makeText(this, "Enter Quantity", Toast.LENGTH_SHORT).show();
        }else{
            uploadToFirebase();
        }

    }

    private void uploadToFirebase() {
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        String uid = firebaseAuth.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", ""+uid);
        hashMap.put("id", ""+timestamp);
        hashMap.put("title", ""+title);
        hashMap.put("description", ""+description);
        hashMap.put("categoryId", ""+selectedCategoryId);
        hashMap.put("timestamp", timestamp);
        hashMap.put("manual", ""+manual);
        hashMap.put("quantity", quantity);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(EquipmentAddActivity.this, "Equipment added success!", Toast.LENGTH_SHORT).show();
                        binding.categoryTv.setText("");
                        binding.titleE.setText("");
                        binding.manualE.setText("");
                        binding.descriptionE.setText("");
                        binding.quantityE.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EquipmentAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadEquipmentCategory() {
        categoryTitleArrayList = new ArrayList<>();
        categoryIdArraylist = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryTitleArrayList.clear();
                categoryIdArraylist.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                   String categoryId = (String) ds.child("id").getValue();
                   String categoryTitle = (String) ds.child("title").getValue();

                   categoryTitleArrayList.add(categoryTitle);
                   categoryIdArraylist.add(categoryId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void imagePickIntent() {
        Log.d(TAG, "imagePickIntent: starting pick image" );

        Intent intent = new Intent();
        intent.setType("image");
    }

    private String selectedCategoryId, selectedCategoryTitle;
    private void categoryPickDialog() {
        String[] categoriesArray = new String[categoryTitleArrayList.size()];
        for(int i = 0; i < categoryTitleArrayList.size(); i++){
            categoriesArray[i] = categoryTitleArrayList.get(i);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedCategoryTitle = categoryTitleArrayList.get(which);
                        selectedCategoryId = categoryIdArraylist.get(which);
                        binding.categoryTv.setText(selectedCategoryTitle);
                    }
                })
                .show();

    }
}