package com.example.oop_project.models;

import com.example.oop_project.models.Person;

public class User extends Person {
    public User(){
        super("user");
    }
    public User(String email, String password, long timestamp, String profileImage, String uid){
        super("user",email, password, timestamp, profileImage, uid);
    }
}
