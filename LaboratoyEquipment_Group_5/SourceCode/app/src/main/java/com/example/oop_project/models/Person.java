package com.example.oop_project.models;

public class Person {
    private String email, password, profileImage, uid;
    private long timestamp;
    public Person(String accountType) {
        this.accountType = accountType;
        this.email = "";
        this.password = "";
        this.timestamp = 0;
        this.profileImage = "";
        this.uid = "";
    }
    public Person(String accountType,String email, String password, long timestamp, String profileImage, String uid) {
        this.timestamp = timestamp;
        this.accountType = accountType;
        this.profileImage = profileImage;
        this.password = password;
        this.email = email;
        this.uid = uid;
    }
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    private String accountType;

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
