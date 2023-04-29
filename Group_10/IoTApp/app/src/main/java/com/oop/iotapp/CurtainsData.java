package com.oop.iotapp;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CurtainsData {
    private String name;
    private Long status;
    private Long percent;
    private Long autoMode;

    public CurtainsData() {
        this.name = "#";
        this.status = 0L;
        this.percent = 0L;
        this.autoMode = 0L;
    }

    public CurtainsData(String name, Long status, Long percent, Long autoMode) {
        this.name = name;
        this.status = status;
        this.percent = percent;
        this.autoMode = autoMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getPercent() {
        return percent;
    }

    public void setPercent(Long percent) {
        this.percent = percent;
    }

    public Long getAutoMode() {
        return autoMode;
    }

    public void setAutoMode(Long autoMode) {
        this.autoMode = autoMode;
    }
}
