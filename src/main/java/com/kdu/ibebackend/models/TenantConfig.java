package com.kdu.ibebackend.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kdu.ibebackend.models.deserializers.GuestsDeserializer;
import com.kdu.ibebackend.models.serializers.GuestsSerializer;
import lombok.Data;

/**
 * Model for Tenant Config stored in DynamoDB
 */
@DynamoDBTable(tableName = "kdu-team1-ibe-dynamo")
@Data
public class TenantConfig {

    @DynamoDBHashKey(attributeName = "tenant_id")
    private Integer tenantId;

    @DynamoDBAttribute(attributeName = "banner_image_url")
    private String bannerImageUrl;

    @DynamoDBAttribute(attributeName = "footer_logo")
    private String footerLogoUrl;

    @DynamoDBAttribute(attributeName = "max_length_stay")
    private Integer maxLengthStay;

    @DynamoDBAttribute(attributeName = "site_logo_url")
    private String siteLogoUrl;

    @DynamoDBAttribute(attributeName = "number_of_rooms")
    private Integer numberOfRooms;

    @DynamoDBAttribute(attributeName = "max_guests")
    private int maxGuests;

    @DynamoDBAttribute(attributeName = "accessibility")
    private boolean accessibility;

    @JsonSerialize(using = GuestsSerializer.class)
    @JsonDeserialize(using = GuestsDeserializer.class)
    private Guests guests;

    @DynamoDBDocument
    @Data
    public static class Guests {
        @DynamoDBAttribute(attributeName = "children")
        @JsonProperty("children")
        private boolean children;

        @DynamoDBAttribute(attributeName = "adults")
        @JsonProperty("adults")
        private boolean adults;

        @DynamoDBAttribute(attributeName = "teens")
        @JsonProperty("teens")
        private boolean teens;
    }
}