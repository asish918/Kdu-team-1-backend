package com.kdu.ibebackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PromotionType {
    @JsonProperty("promotion_description")
    private String promotionDescription;

    @JsonProperty("promotion_id")
    private String promotionId;

    @JsonProperty("promotion_title")
    private String promotionTitle;

    @JsonProperty("price_factor")
    private Double priceFactor;

    @JsonProperty("minimum_days_of_stay")
    private Integer minimumDaysOfStay;
}
