package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomTypeRate {
    @JsonProperty("room_type_id")
    private Integer room_type_id;

    @JsonProperty("room_type_name")
    private String room_type_name;
}
