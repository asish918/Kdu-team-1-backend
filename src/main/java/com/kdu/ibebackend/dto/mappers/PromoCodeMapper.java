package com.kdu.ibebackend.dto.mappers;

import com.kdu.ibebackend.dto.response.PromoCodeDTO;
import com.kdu.ibebackend.entities.PromoCode;
import lombok.extern.slf4j.Slf4j;

public class PromoCodeMapper {
    public static PromoCodeDTO entityToDTO(PromoCode promoCode) {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO();

        promoCodeDTO.setPromoTitle(promoCode.getPromoTitle());
        promoCodeDTO.setPromoDescription(promoCode.getPromoDesc());
        promoCodeDTO.setPriceFactor(promoCode.getPriceFactor());

        return promoCodeDTO;
    }
}