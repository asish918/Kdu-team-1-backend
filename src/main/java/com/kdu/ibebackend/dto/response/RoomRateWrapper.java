package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdu.ibebackend.models.RoomRate;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class RoomRateWrapper {
    @JsonProperty("room_rate")
    private RoomRate roomRate;

    @JsonProperty("room_type")
    private RoomType roomType;
}
