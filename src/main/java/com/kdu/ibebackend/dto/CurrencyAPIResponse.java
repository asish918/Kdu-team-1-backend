package com.kdu.ibebackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Response for Exchange Rates API
 */
@Data
@JsonIgnoreProperties
public class CurrencyAPIResponse {
    @JsonProperty("data")
    private Data data;

    @JsonIgnoreProperties
    public static class Data {
        @JsonProperty("EUR")
        private double eur;
        @JsonProperty("INR")
        private double inr;
        @JsonProperty("USD")
        private double usd;
    }
}
