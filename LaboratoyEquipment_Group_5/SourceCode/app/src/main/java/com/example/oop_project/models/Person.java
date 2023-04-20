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

public class Person {
    protected String email, password, profileImage, uid;
    protected long timestamp;
    protected String fullName, mobile, dOB, otherInfor, address;
    protected int gender;
    protected String birthday;
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
        birthday = "";
    }

    public Person(String accountType, String email, String password, long timestamp, String profileImage, String uid) {
        this.timestamp = timestamp;
        this.accountType = accountType;
        this.profileImage = profileImage;
        this.password = password;
        this.email = email;
        this.uid = uid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

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
    protected  void sendMail(Context context, String uid, String subject, String message){

    };

    public Task<Person> getDataFromFireBase(String uid){
        TaskCompletionSource<Person> taskCompletionSource = new TaskCompletionSource<>();
        Person person = new Person(this.accountType);
        person.setUid(uid);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("email")){
                    email = "" + snapshot.child("email").getValue();
                }
                if(snapshot.hasChild("fullName")){
                    fullName = "" + snapshot.child("fullName").getValue();
                }
                if(snapshot.hasChild("profileImage")){
                    profileImage = "" + snapshot.child("profileImage").getValue();
                }
                if(snapshot.hasChild("accountType")){
                    accountType = "" + snapshot.child("accountType").getValue();
                }
                if(snapshot.hasChild("mobile")){
                    mobile = "" + snapshot.child("mobile").getValue();
                }
                if(snapshot.hasChild("address")){
                    address = "" + snapshot.child("address").getValue();
                }
                if(snapshot.hasChild("otherInfor")){
                    otherInfor = "" + snapshot.child("otherInfor").getValue();
                }
                if(snapshot.hasChild("birthday")){
                    birthday = "" + snapshot.child("birthday").getValue();
                }
                if(snapshot.hasChild("gender")){
                    gender =Integer.parseInt("" + snapshot.child("gender").getValue());
                }
                person.setEmail(email);
                person.setFullName(fullName);
                person.setProfileImage(profileImage);
                person.setUid(uid);
                person.setAccountType(accountType);
                person.setMobile(mobile);
                person.setAddress(address);
                person.setOtherInfor(otherInfor);
                person.setBirthday(birthday);
                person.setGender(gender);
                taskCompletionSource.setResult(person);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                taskCompletionSource.setException(error.toException());
            }
        });
        return taskCompletionSource.getTask();

    }
}
