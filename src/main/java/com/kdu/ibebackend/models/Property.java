package com.kdu.ibebackend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * JSON Model for Property data type
 */
@Data
@AllArgsConstructor
public class Property {
    @JsonProperty("property_name")
    private String propertyName;

    @JsonProperty("property_id")
    private Integer propertyId;
}
