package com.example.oop_project.models;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModelEquipment {
    private String key;
    private String uid, id, title, description, categoryId, manual;
    private String isUsedBy;
    private String equipmentImage;
    private long timestamp;
    private int quantity;
    private int viewed;
    private String status;
    private boolean isFavorite;
    private int quantityBorrow;
    private String adminStatus;
    private String preStatus;

    public ModelEquipment() {
        this.adminStatus = "";
        this.description = "";
        this.categoryId = "";
        this.manual = "";
        this.isUsedBy = "user";
        this.preStatus = "";
        this.title = "";
        this.equipmentImage = "";
        this.quantity = 0;
        this.uid = "";
        this.key = "";
        this.quantityBorrow = 0;
        this.isFavorite = false;
    }

    public ModelEquipment(String uid, String id, String title, String description, String categoryId, String manual, long timestamp, int quantity) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.manual = manual;
        this.timestamp = timestamp;
        this.quantity = quantity;
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsUsedBy() {
        return isUsedBy;
    }

    public void setIsUsedBy(String isUsedBy) {
        this.isUsedBy = isUsedBy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getQuantityBorrow() {
        return quantityBorrow;
    }

    public void setQuantityBorrow(int quantityBorrow) {
        this.quantityBorrow = quantityBorrow;
    }

    public String getEquipmentImage() {
        return equipmentImage;
    }

    public void setEquipmentImage(String equipmentImage) {
        this.equipmentImage = equipmentImage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }


    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getManual() {
        return manual;
    }

    public void setManual(String manual) {
        this.manual = manual;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Task<String> getTitle(String equipmentId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();
        ref.child(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullName = "" + snapshot.child("title").getValue();
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

    public Task<ModelEquipment> getDataFromFireBase(String equipmentId) {
        TaskCompletionSource<ModelEquipment> taskCompletionSource = new TaskCompletionSource<>();
        ModelEquipment modelEquipment = new ModelEquipment();
        modelEquipment.setId(equipmentId);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(this.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("categoryId")) {
                    categoryId = "" + snapshot.child("cateogryId").getValue();
                }
                if (snapshot.hasChild("description")) {
                    description = "" + snapshot.child("description").getValue();
                }
                if (snapshot.hasChild("equipmentImage")) {
                    equipmentImage = "" + snapshot.child("equipmentImage").getValue();
                }
                if (snapshot.hasChild("manual")) {
                    manual = "" + snapshot.child("manual").getValue();
                }
                if (snapshot.hasChild("quantity")) {
                    quantity = Integer.parseInt("" + snapshot.child("quantity").getValue());
                }
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
                modelEquipment.setCategoryId(categoryId);
                modelEquipment.setDescription(description);
                modelEquipment.setEquipmentImage(equipmentImage);
                modelEquipment.setManual(manual);
                modelEquipment.setQuantity(quantity);
                modelEquipment.setStatus(status);
                modelEquipment.setTimestamp(timestamp);
                modelEquipment.setTitle(title);
                modelEquipment.setUid(uid);
                taskCompletionSource.setResult(modelEquipment);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });
        return taskCompletionSource.getTask();

    }

}
