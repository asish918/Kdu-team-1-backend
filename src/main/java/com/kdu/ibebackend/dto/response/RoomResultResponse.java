package com.kdu.ibebackend.dto.response;

import com.kdu.ibebackend.models.PromotionType;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class RoomResultResponse {
    private Integer area_in_square_feet;

    private Integer double_bed;

    private Integer max_capacity;

    private String room_type_name;

    private Integer single_bed;

    private Double average_rate;

    private Integer room_type_id;

    private Double rating;

    private List<String> reviews;

    private List<String> highResImages;

    private List<String> lowResImages;

    private PromotionType promotionType;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        RoomResultResponse roomType = (RoomResultResponse) obj;
        return Objects.equals(room_type_id, roomType.room_type_id);
    }

    @Override
    public int hashCode() {
        return room_type_id;
    }
}
