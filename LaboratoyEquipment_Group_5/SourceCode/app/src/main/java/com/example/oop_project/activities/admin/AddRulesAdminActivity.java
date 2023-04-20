package com.example.oop_project.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.oop_project.MyApplication;
import com.example.oop_project.activities.common.SuccessActivity;
import com.example.oop_project.databinding.ActivityAddRulesAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddRulesAdminActivity extends AppCompatActivity {
    private ActivityAddRulesAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddRulesAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        loadContent();
       binding.submitBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String text = binding.editText.getText().toString();
               MyApplication.ConvertMarkDown(AddRulesAdminActivity.this, text);
               Intent intent = new Intent(AddRulesAdminActivity.this, SuccessActivity.class);
               intent.putExtra("status", "True");
               startActivity(intent);
           }
       });

    }


    private void loadContent() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Rules");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String content = "" + snapshot.child("content").getValue();
                binding.editText.setText(content);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}