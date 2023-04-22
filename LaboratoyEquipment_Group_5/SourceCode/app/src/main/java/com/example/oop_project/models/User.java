package com.example.oop_project.models;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.oop_project.MyApplication;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User extends Person {
    public User() {
        super("user");
    }

    public User(String email, String password, long timestamp, String profileImage, String uid) {
        super("user", email, password, timestamp, profileImage, uid);
    }

    @Override
    public void sendMail(Context context, String uid, String subject, String message) {
        this.uid = uid;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = "" + snapshot.child("email").getValue();
                MyApplication.sendMail(context, email, subject, message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public Task<Integer> getAmountOfBorrowed(String uid, String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();
        ref.child(uid)
                .child("Borrowed")
                .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Integer quantityBorrowed = Integer.parseInt("" + snapshot.child("quantityBorrowed").getValue());
                        if (quantityBorrowed != null) {
                            taskCompletionSource.setResult(quantityBorrowed);
                        } else {
                            // Xử lý trường hợp không có dữ liệu
                            taskCompletionSource.setException(new Exception("Không tìm thấy dữ liệu"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu có
                        taskCompletionSource.setException(error.toException());
                    }
                });

        return taskCompletionSource.getTask();
    }

    public Task<String> getFullName(String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = "" + snapshot.child("fullName").getValue();
                if (fullName.equals("null")) {
                    taskCompletionSource.setException(new Exception("Chưa có tên"));
                } else {
                    taskCompletionSource.setResult(fullName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });

        return taskCompletionSource.getTask();
    }

}
