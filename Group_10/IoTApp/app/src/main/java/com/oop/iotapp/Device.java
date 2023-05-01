package com.oop.iotapp;

public class Device {
    private String address;
    private Long port;

    public Device() {

    }

    public Device(String address, Long port) {
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }
}
