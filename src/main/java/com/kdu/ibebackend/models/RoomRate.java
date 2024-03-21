package com.kdu.ibebackend.models;

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
    private String date;
    private double basic_nightly_rate;
    private int room_rate_id;
}