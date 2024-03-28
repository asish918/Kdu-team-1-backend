package com.kdu.ibebackend.dto.response;

import lombok.Data;

@Data
public class PromoCodeDTO {
    private String promoTitle;
    private String promoDescription;
    private Double priceFactor;
}
