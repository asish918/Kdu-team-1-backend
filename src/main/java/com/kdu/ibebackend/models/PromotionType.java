package com.kdu.ibebackend.models;


import lombok.Data;

@Data
public class PromotionType {
    private String promotion_description;
    private String promotion_id;
    private String promotion_title;
    private Double price_factor;
}
