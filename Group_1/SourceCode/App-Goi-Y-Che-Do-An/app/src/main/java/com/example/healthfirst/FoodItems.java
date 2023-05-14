package com.example.healthfirst;

public class FoodItems {
    String foodName;
    String foodNameEng;
    @Override
    public String toString() {
        return foodName;
    }

    public FoodItems(String foodName, String foodNameEng,Integer foodImageId) {
        this.foodName = foodName;
        this.foodImageId = foodImageId;
        this.foodNameEng = foodNameEng;
    }

    public String getFoodName() {
        return foodName;
    }
    public String getFoodNameEng() {
        return foodNameEng;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Integer getFoodImageId() {
        return foodImageId;
    }

    public void setFoodImageId(Integer foodImageId) {
        this.foodImageId = foodImageId;
    }

    Integer foodImageId;
}
