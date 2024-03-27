package com.kdu.ibebackend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kdu.ibebackend.constants.ValidationConstants;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Validated DTO for getting Search and Filter parameters from Frontend
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties
public class SearchParamDTO {
    @Pattern(regexp = ValidationConstants.DATE_FORMAT_REGEX, message = ValidationConstants.DATE_FORMAT_MESSAGE)
    @NotNull(message = ValidationConstants.START_DATE_NOT_NULL_MESSAGE)
    private String startDate;

    @Pattern(regexp = ValidationConstants.DATE_FORMAT_REGEX, message = ValidationConstants.DATE_FORMAT_MESSAGE)
    @NotNull(message = ValidationConstants.END_DATE_NOT_NULL_MESSAGE)
    private String endDate;

    @PositiveOrZero(message = ValidationConstants.BEDS_POSITIVE_MESSAGE)
    @NotNull(message = ValidationConstants.BEDS_NOT_NULL_MESSAGE)
    private Integer beds = 1;

    @Positive(message = ValidationConstants.ROOMS_POSITIVE_MESSAGE)
    @NotNull(message = ValidationConstants.ROOMS_NOT_NULL_MESSAGE)
    private Integer rooms;

    @Positive(message = ValidationConstants.PROPERTY_ID_POSITIVE_MESSAGE)
    @NotNull(message = ValidationConstants.PROPERTY_ID_NOT_NULL_MESSAGE)
    private Integer propertyId;

    @Positive(message = ValidationConstants.TOTAL_GUESTS_POSITIVE_MESSAGE)
    @NotNull(message = ValidationConstants.TOTAL_GUESTS_NOT_NULL_MESSAGE)
    private Integer totalGuests;

    @NotNull
    private List<Integer> roomTypes;
    @NotNull
    private List<String> bedTypes;
    @Nullable
    private Boolean priceSort;
    @Nullable
    private Boolean areaSort;
    @Nullable
    private Boolean ratingSort;

    @Nullable
    private Boolean isSeniorCitizen;
    @Nullable
    private Boolean isKDUMember;
    @Nullable
    private Boolean isMilitary;
}
