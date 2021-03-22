package com.example.marketmaker.dto;

public class InstrumentPrice {

    private int id;

    private String side;

    private int quat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getQuat() {
        return quat;
    }

    public void setQuat(int quat) {
        this.quat = quat;
    }
}
