package com.staszp.dto.incommingmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RateModelDto {

    private String currency;
    private String code;
    private float mid;

    public RateModelDto(String currency, String code, float mid) {
        this.currency = currency;
        this.code = code;
        this.mid = mid;
    }

    public RateModelDto() {
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("mid")
    public float getMid() {
        return mid;
    }

    public void setMid(float mid) {
        this.mid = mid;
    }
}
