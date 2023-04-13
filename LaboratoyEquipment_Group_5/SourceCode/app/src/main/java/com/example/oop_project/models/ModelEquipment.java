package com.example.oop_project.models;

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
    public ModelEquipment() {
        this.description = "";
        this.title = "";
        this.categoryId = "";
        this.manual = "";
        isUsedBy = "user";
        this.quantity = 0;
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
}
