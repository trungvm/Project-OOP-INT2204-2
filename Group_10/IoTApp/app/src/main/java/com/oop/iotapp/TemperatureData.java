package com.oop.iotapp;

public class TemperatureData {
    private Long status, temperature, fanSpeed, autoMode, timerMode;
    private String timerStart, timerStop, name;

    public TemperatureData(){
        this.name = "#";
        this.status = 0L;
        this.temperature = 0L;
        this.fanSpeed = 0L;
        this.autoMode = 0L;
        this.timerMode = 0L;
        this.timerStart = "#";
        this.timerStop = "#";
    }

    public TemperatureData(String name, Long status, Long temperature, Long fanSpeed, Long autoMode, Long timerMode, String timerStart, String timerStop) {
        this.name = name;
        this.status = status;
        this.temperature = temperature;
        this.fanSpeed = fanSpeed;
        this.autoMode = autoMode;
        this.timerMode = timerMode;
        this.timerStart = timerStart;
        this.timerStop = timerStop;
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

    public Long getTemperature() {
        return temperature;
    }

    public void setTemperature(Long temperature) {
        this.temperature = temperature;
    }

    public Long getFanSpeed() {
        return fanSpeed;
    }

    public void setFanSpeed(Long fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public Long getAutoMode() {
        return autoMode;
    }

    public void setAutoMode(Long autoMode) {
        this.autoMode = autoMode;
    }

    public Long getTimerMode() {
        return timerMode;
    }

    public void setTimerMode(Long timerMode) {
        this.timerMode = timerMode;
    }

    public String getTimerStart() {
        return timerStart;
    }

    public void setTimerStart(String timerStart) {
        this.timerStart = timerStart;
    }

    public String getTimerStop() {
        return timerStop;
    }

    public void setTimerStop(String timerStop) {
        this.timerStop = timerStop;
    }
}
