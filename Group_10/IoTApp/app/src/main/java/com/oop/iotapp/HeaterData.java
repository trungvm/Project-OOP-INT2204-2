package com.oop.iotapp;

public class HeaterData extends Device{
    private Long status, heater, timerMode;
    private String timerStart, timerStop, name;

    public HeaterData(String newDevice, long port, long status, long heater, long timerMode, long l, String timerStop, String name){
        this.setAddress("0.0.0.0");
        this.setPort(0L);
        this.name = "#";
        this.status = 0L;
        this.heater = 0L;
        this.timerMode = 0L;
        this.timerStart = "#";
        this.timerStop = "#";
    }

    public HeaterData(String name, Long status, Long heater, Long timerMode, String timerStart, String timerStop) {
        this.name = name;
        this.status = status;
        this.heater = heater;
        this.timerMode = timerMode;
        this.timerStart = timerStart;
        this.timerStop = timerStop;
    }

    public HeaterData(String address, Long port, Long status, Long heater, Long timerMode, String timerStart, String timerStop, String name) {
        super(address, port);
        this.status = status;
        this.heater = heater;
        this.timerMode = timerMode;
        this.timerStart = timerStart;
        this.timerStop = timerStop;
        this.name = name;
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

    public Long getHeater() {
        return heater;
    }

    public void setHeater(Long heater) {
        this.heater = heater;
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
