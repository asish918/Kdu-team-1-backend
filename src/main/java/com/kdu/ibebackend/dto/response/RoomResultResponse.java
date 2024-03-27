package com.kdu.ibebackend.dto.response;

import com.kdu.ibebackend.models.PromotionType;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * DTO for sending data to room results page
 */
@Data
public class RoomResultResponse {
    private Integer areaInSquareFeet;

    private Integer doubleBed;

    private Integer maxCapacity;

    private String roomTypeName;

    private Integer singleBed;

    private Double averageRate;

    private Integer roomTypeId;

    private Double rating;

    private List<String> reviews;

    private List<String> highResImages;

    private List<String> lowResImages;

    private PromotionType bestPromotion;

    private List<PromotionType> validPromotions;

    private List<String> amenities;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        RoomResultResponse roomType = (RoomResultResponse) obj;
        return Objects.equals(roomTypeId, roomType.roomTypeId);
    }

    @Override
    public int hashCode() {
        return roomTypeId;
    }
}
