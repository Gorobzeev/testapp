package com.staszp.dto.incommingmodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IncomeRateModelDto {

    private String effectiveDate;
    private List<RateModelDto> rates;

    public IncomeRateModelDto() {
    }

    public IncomeRateModelDto(String effectiveDate, List<RateModelDto> rates) {
        this.effectiveDate = effectiveDate;
        this.rates = rates;
    }

    @JsonProperty("effectiveDate")
    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @JsonProperty("rates")
    public List<RateModelDto> getRates() {
        return rates;
    }

    public void setRates(List<RateModelDto> rates) {
        this.rates = rates;
    }
}
