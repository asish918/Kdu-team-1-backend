package com.kdu.ibebackend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class SearchParamDTO {
    private String startDate;
    private String endDate;
    private Integer beds = 1;
    private Integer rooms;
    private Integer propertyId;
    private Integer totalGuests;
    private List<Integer> roomTypes;
    private List<String> bedTypes;
    private Boolean priceSort;
    private Boolean areaSort;
    private Boolean ratingSort;
}
