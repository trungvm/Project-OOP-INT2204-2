package com.example.oop_project.activities.common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.oop_project.activities.user.DashboardUserActivity;
import com.example.oop_project.databinding.ActivityRegisterBinding;
import com.example.oop_project.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    // firebase Auth
    private FirebaseAuth firebaseAuth;

    // progress dialog
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait!");
        progressDialog.setCanceledOnTouchOutside(false);

        // handle click backLogin
        binding.backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
         User user = new User();
        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData(user);
            }
        });
    }



    private void getData(User user){
        String email = "", password = "";
        email = binding.email.getText().toString().trim();
        password = binding.password.getText().toString().trim();
        user.setEmail(email);
        user.setPassword(password);
    }
    private void validateData(User user) {
        getData(user);
        // before creating, need validate data
        String confirmPassword = binding.confirmPassword.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
            Toast.makeText(this, "Invalid email...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(user.getPassword())) {
            Toast.makeText(this, "Enter password...!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Enter confirm password...!", Toast.LENGTH_SHORT).show();
        }
        else if(!user.getPassword().equals(confirmPassword)){
            Toast.makeText(this, "Confirm password doesn't match...!", Toast.LENGTH_SHORT).show();
        }else{
            createUserAccount(user);
        }
    }

    private void createUserAccount(User user) {
        //show progress
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        // create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // account creation success, now add realtime firebase
                        updateUserInfo(user);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // acount creation failed
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void updateUserInfo(User user) {
        progressDialog.setMessage("Saving user information...");

        // timestamp
        long timestamp = System.currentTimeMillis();
        user.setTimestamp(timestamp);
        // get current user uid
        String uid = firebaseAuth.getUid();
        user.setProfileImage("");
        user.setUid(uid);
        // setup data to add in db
        // set data to db;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("email", user.getEmail());
        hashMap.put("accountType", user.getAccountType());
        hashMap.put("uid", user.getUid());
        hashMap.put("timestamp", user.getTimestamp());
        hashMap.put("profileImage", user.getProfileImage());
        hashMap.put("mobile", user.getMobile());
        hashMap.put("address", user.getAddress());
        hashMap.put("fullName", user.getFullName());
        hashMap.put("otherInfo", user.getOtherInfor());
        hashMap.put("birthday", user.getdOB());
        hashMap.put("gender", user.getGender());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // data added to db
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Account created...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, DashboardUserActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // push failed
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}