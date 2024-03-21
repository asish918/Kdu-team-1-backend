package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomRateWrapper {
    @JsonProperty("room_rate")
    private RoomRate roomRate;

    @JsonProperty("room_type")
    private RoomTypeRate roomTypeRate;
}
