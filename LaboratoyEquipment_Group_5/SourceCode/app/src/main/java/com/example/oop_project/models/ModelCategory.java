package com.example.oop_project.models;

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

}
