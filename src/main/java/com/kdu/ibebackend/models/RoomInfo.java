package com.kdu.ibebackend.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

import java.util.List;

@Data
@DynamoDBTable(tableName = "kdu-team1-ibe-roomtypeinfo")
public class RoomInfo {
    @DynamoDBHashKey(attributeName = "room_type_id")
    private Integer roomTypeId;

    @DynamoDBAttribute(attributeName = "room_rating")
    private Double roomRating;

    @DynamoDBAttribute(attributeName = "room_reviews")
    private List<String> roomReviews;

    @DynamoDBAttribute(attributeName = "low_res_images")
    private List<String> lowResImages;

    @DynamoDBAttribute(attributeName = "high_res_images")
    private List<String> highResImages;
}
