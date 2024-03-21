package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FinalResponse {
    private Integer area_in_square_feet;

    private Integer double_bed;

    private Integer max_capacity;

    private String room_type_name;

    private Integer single_bed;

    private Double average_rate;
}
