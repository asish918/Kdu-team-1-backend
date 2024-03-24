package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class RoomType {
    @JsonProperty("area_in_square_feet")
    private Integer area_in_square_feet;

    @JsonProperty("double_bed")
    private Integer double_bed;

    @JsonProperty("max_capacity")
    private Integer max_capacity;

    @JsonProperty("property_id")
    private Integer property_id;

    @JsonProperty("room_type_name")
    private String room_type_name;

    @JsonProperty("single_bed")
    private Integer single_bed;

    @JsonProperty("room_type_id")
    private Integer room_type_id;
}
