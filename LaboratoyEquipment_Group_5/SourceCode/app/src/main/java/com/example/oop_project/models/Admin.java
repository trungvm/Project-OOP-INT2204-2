package com.example.oop_project.models;

public class Admin extends Person {
    public Admin() {
        super("admin");
    }

    public Admin(String email, String password, long timestamp, String profileImage, String uid) {
        super("admin", email, password, timestamp, profileImage, uid);
    }
}
