package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RoomRate {
    @JsonProperty("basic_nightly_rate")
    private Integer basic_nightly_rate;

    @JsonProperty("date")
    private String date;
}
