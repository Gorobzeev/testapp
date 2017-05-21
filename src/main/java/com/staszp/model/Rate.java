package com.staszp.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "rates")
public class Rate {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "time")
    private String time;

    @Column(name = "rate")
    private String rate;

    public Rate() {
    }

    public Rate(String time, String rate) {
        this.time = time;
        this.rate = rate;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @JsonProperty("rate")
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
