package com.example.oop_project.activities.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.oop_project.databinding.ActivityCategoryAddBinding;
import com.example.oop_project.models.ModelCategory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
               onBackPressed();
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
        String position = binding.position.getText().toString().trim();
        category.setTitle(title);
        category.setPosition(position);
    }
    private void validateData(ModelCategory category) {
        getData(category);
        if(TextUtils.isEmpty(category.getTitle())){
            Toast.makeText(this, "Vui lòng nhập tên thể loại", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(category.getPosition())){
            Toast.makeText(this, "Vui lòng nhập vị trí", Toast.LENGTH_SHORT).show();
        }else {
            validateCategoryTitle(category);
        }
    }

    String title;
    boolean  flag = false;
    public interface OnCheckTitleListener {
        void onTitleChecked(boolean flag);
    }
    private void validateCategoryTitle(ModelCategory category) {
        title = category.getTitle();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    String oldTitle = "" + ds.child("title").getValue();
                    String status = "" + ds.child("status").getValue();
                    if(title.equals(oldTitle) && status.equals("use")){
                        Toast.makeText(CategoryAddActivity.this, "Thể loại đã trùng tên", Toast.LENGTH_SHORT).show();
                        return;
                    };
                }
                addCategoryFirebase(category);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                        binding.position.setText("");
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