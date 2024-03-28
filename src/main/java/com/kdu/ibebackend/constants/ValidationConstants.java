package com.kdu.ibebackend.constants;

/**
 * Messages for DTO Level validation of Request Body
 */
public class ValidationConstants {
    public static final String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z";
    public static final String DATE_FORMAT_MESSAGE = "Date should be in yyyy-MM-dd'T'HH:mm:ss.SSS'Z' format";
    public static final String START_DATE_NOT_NULL_MESSAGE = "Start date cannot be null";
    public static final String END_DATE_NOT_NULL_MESSAGE = "End date cannot be null";
    public static final String BEDS_POSITIVE_MESSAGE = "Number of beds must be more than zero";
    public static final String BEDS_NOT_NULL_MESSAGE = "Beds cannot be null";
    public static final String ROOMS_POSITIVE_MESSAGE = "Number of rooms must be more than zero";
    public static final String ROOMS_NOT_NULL_MESSAGE = "Rooms cannot be null";
    public static final String PROPERTY_ID_POSITIVE_MESSAGE = "Property Id must be positive";
    public static final String PROPERTY_ID_NOT_NULL_MESSAGE = "Property ID cannot be null";
    public static final String TOTAL_GUESTS_POSITIVE_MESSAGE = "Total guests must be more than zero";
    public static final String TOTAL_GUESTS_NOT_NULL_MESSAGE = "Total guests cannot be null";
    public static final String INVALID_PROMO = "The Promo Code is invalid";
}
