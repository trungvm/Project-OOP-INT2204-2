package com.example.oop_project.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModelCategory {
    private String title, uid, id;
    private String position;
    private long timestamp;
    private String status;

    public ModelCategory() {
        status = "use";
        position = "";
        this.uid = "";
        this.title = "";
        this.id = "";
        this.timestamp = 0;
    }

    public ModelCategory(String id, String title, String uid, long timestamp) {
        this.id = id;
        this.title = title;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Task<ModelCategory> getDataFromFireBase(String categoryId) {
        TaskCompletionSource<ModelCategory> taskCompletionSource = new TaskCompletionSource<>();
        ModelCategory modelCategory = new ModelCategory();
        modelCategory.setId(categoryId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("status")) {
                    status = "" + snapshot.child("status").getValue();
                }
                if (snapshot.hasChild("timestamp")) {
                    timestamp = Long.parseLong("" + snapshot.child("timestamp").getValue());
                }
                if (snapshot.hasChild("title")) {
                    title = "" + snapshot.child("title").getValue();
                }
                if (snapshot.hasChild("uid")) {
                    uid = "" + snapshot.child("uid").getValue();
                }
                if (snapshot.hasChild("position")) {
                    position = "" + snapshot.child("position").getValue();
                }
                modelCategory.setPosition(position);
                modelCategory.setStatus(status);
                modelCategory.setTimestamp(timestamp);
                modelCategory.setTitle(title);
                modelCategory.setUid(uid);
                taskCompletionSource.setResult(modelCategory);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });
        return taskCompletionSource.getTask();

    }

}
