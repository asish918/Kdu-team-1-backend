package com.kdu.ibebackend.models.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "kdu-team1-ibe-roomreviews")
public class RoomReview {
    @DynamoDBHashKey(attributeName = "review_id")
    private String reviewId;

    @DynamoDBAttribute(attributeName = "rating")
    private Double rating;

    @DynamoDBAttribute(attributeName = "review")
    private String review;

    @DynamoDBAttribute(attributeName = "user_email")
    private String userEmail;

    @DynamoDBAttribute(attributeName = "room_type_id")
    private Integer roomTypeId;
}
