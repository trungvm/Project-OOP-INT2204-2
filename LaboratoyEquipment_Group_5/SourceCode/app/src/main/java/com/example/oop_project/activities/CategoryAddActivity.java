package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.oop_project.databinding.ActivityCategoryAddBinding;
import com.example.oop_project.models.ModelCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CategoryAddActivity extends AppCompatActivity {
    private ActivityCategoryAddBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(CategoryAddActivity.this, EquipmentManagerActivity.class));
               finish();
            }
        });
        //handle click being upload category
        ModelCategory category = new ModelCategory();
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(category);
            }
        });
    }
    private void getData(ModelCategory category){
        String title = binding.category.getText().toString().trim();
        category.setTitle(title);
    }
    private void validateData(ModelCategory category) {
        getData(category);
        if(TextUtils.isEmpty(category.getTitle())){
            Toast.makeText(this, "Please enter category!", Toast.LENGTH_SHORT).show();
        }else{
            addCategoryFirebase(category);
        }
    }

    private void addCategoryFirebase(ModelCategory category) {
        progressDialog.setMessage("Adding category...");
        progressDialog.show();

        long timestamp = System.currentTimeMillis();
        category.setTimestamp(timestamp);
        category.setId(""+timestamp);
        category.setUid(firebaseAuth.getUid());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(""+timestamp)
                .setValue(category)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, "Category added success!", Toast.LENGTH_SHORT).show();
                        binding.category.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryAddActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}