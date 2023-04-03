package com.example.oop_project.models;

public class Person {
    private String email, password, profileImage, uid;
    private long timestamp;
    private String fullName, mobile, dOB, otherInfor, address;
    private int gender;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getdOB() {
        return dOB;
    }

    public void setdOB(String dOB) {
        this.dOB = dOB;
    }

    public String getOtherInfor() {
        return otherInfor;
    }

    public void setOtherInfor(String otherIfor) {
        this.otherInfor = otherIfor;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Person(String accountType) {
        this.accountType = accountType;
        this.email = "";
        this.password = "";
        this.timestamp = 0;
        this.profileImage = "";
        this.uid = "";
        this.fullName = "";
        this.mobile = "";
        this.dOB = "";
        this.gender = 0;
        this.otherInfor = "";
        this.address = "";
    }

    public Person(String accountType, String email, String password, long timestamp, String profileImage, String uid) {
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
