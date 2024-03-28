package com.kdu.ibebackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON Model for RoomRate data type
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRate {
    @JsonProperty("date")
    private String date;

    @JsonProperty("basic_nightly_rate")
    private double basicNightlyRate;

    @JsonProperty("room_rate_id")
    private int roomRateId;
}
