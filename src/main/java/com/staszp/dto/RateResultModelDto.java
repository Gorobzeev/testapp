package com.staszp.dto;

public class RateResultModelDto {

    private String date;
    private float rate;

    public RateResultModelDto(String date, float rate) {
        this.date = date;
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
