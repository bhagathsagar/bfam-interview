package com.example.marketmaker.dto;

public class InstrumentQuotedPrice extends InstrumentPrice {
    double quotedPrice;

    public double getQuotedPrice() {
        return quotedPrice;
    }

    public void setQuotedPrice(double quotedPrice) {
        this.quotedPrice = quotedPrice;
    }
}
