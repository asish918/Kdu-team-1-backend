package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class RoomType {
    @JsonProperty("area_in_square_feet")
    private Integer areaInSquareFeet;

    @JsonProperty("double_bed")
    private Integer doubleBed;

    @JsonProperty("max_capacity")
    private Integer maxCapacity;

    @JsonProperty("property_id")
    private Integer propertyId;

    @JsonProperty("room_type_name")
    private String roomTypeName;

    @JsonProperty("single_bed")
    private Integer singleBed;

    @JsonProperty("room_type_id")
    private Integer roomTypeId;
}
