package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdu.ibebackend.models.PromotionType;
import lombok.Data;

import java.util.List;

@Data
public class PromotionData {
    @JsonProperty("data")
    public Res res;

    @Data
    public static class Res {
        @JsonProperty("listPromotions")
        private List<PromotionType> promotionTypeList;
    }
}
