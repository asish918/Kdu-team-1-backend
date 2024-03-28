package com.kdu.ibebackend.dto.mappers;

import com.kdu.ibebackend.dto.request.RoomReviewDTO;
import com.kdu.ibebackend.models.dynamodb.RoomReview;

import java.util.UUID;

public class RoomReviewMapper {
    public static RoomReview dtoToEntity(RoomReviewDTO roomReviewDTO) {
        UUID uuid = UUID.randomUUID();

        RoomReview roomReview = new RoomReview();
        roomReview.setReviewId(uuid.toString());
        roomReview.setRating(roomReviewDTO.getRating());
        roomReview.setReview(roomReviewDTO.getReview());
        roomReview.setUserEmail(roomReviewDTO.getUserEmail());
        roomReview.setRoomTypeId(roomReviewDTO.getRoomTypeId());

        return roomReview;
    }
}
