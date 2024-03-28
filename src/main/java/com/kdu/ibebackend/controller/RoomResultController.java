package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.constants.Constants;
import com.kdu.ibebackend.dto.request.RoomReviewDTO;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.PromoCodeDTO;
import com.kdu.ibebackend.exceptions.custom.InvalidPromoException;
import com.kdu.ibebackend.service.DynamoDBService;
import com.kdu.ibebackend.service.PromotionService;
import com.kdu.ibebackend.service.RoomResultService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/roomresult")
public class RoomResultController {
    private RoomResultService roomResultService;
    private PromotionService promotionService;
    private DynamoDBService dynamoDBService;

    @Autowired
    public RoomResultController(RoomResultService roomResultService, PromotionService promotionService, DynamoDBService dynamoDBService) {
        this.roomResultService = roomResultService;
        this.promotionService = promotionService;
        this.dynamoDBService = dynamoDBService;
    }

    @PostMapping("/search")
    public ResponseEntity<Object> searchResults(@RequestBody @Valid SearchParamDTO searchParamDTO,
                                                           @RequestParam(defaultValue = "0") @Min(0) @NotNull @Valid int page,
                                                           @RequestParam(defaultValue = "10") @Min(1) @NotNull @Valid int size) {

        return roomResultService.paginatedData(searchParamDTO, page, size);
    }

    @GetMapping("/promotion")
    public ResponseEntity<PromoCodeDTO> promoCode(@RequestParam @NotNull @Valid String promoCode, @RequestParam @NotNull @Valid Integer roomTypeId) throws InvalidPromoException {
        return promotionService.validatePromoCode(promoCode, roomTypeId);
    }

    @PostMapping("/review")
    public ResponseEntity<String> addRoomReview(@RequestBody @Valid RoomReviewDTO roomReviewDTO) {
        dynamoDBService.saveRoomReview(roomReviewDTO);
        return new ResponseEntity<>(Constants.DYNAMODB_SUCCESS, HttpStatus.OK);
    }
}
