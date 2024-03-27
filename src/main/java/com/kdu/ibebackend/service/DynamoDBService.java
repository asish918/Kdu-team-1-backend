package com.kdu.ibebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.constants.Errors;
import com.kdu.ibebackend.models.dynamodb.RoomInfo;
import com.kdu.ibebackend.models.dynamodb.TenantConfig;
import com.kdu.ibebackend.repository.DynamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service Layer for interacting with DynamoDB
 */
@Service
public class DynamoDBService {

    private final DynamoRepository dynamoRepository;

    @Autowired
    public DynamoDBService(DynamoRepository dynamoRepository) {
        this.dynamoRepository = dynamoRepository;
    }

    public ResponseEntity<Object> fetchTenantConfig(Number tenantId) {
        try {
            TenantConfig tenantConfig = dynamoRepository.getTenantConfig(tenantId);
            return new ResponseEntity<>(tenantConfig, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(Errors.TENANT_CONFIG_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public RoomInfo fetchRoomInfo(Number roomTypeId) throws JsonProcessingException {
            RoomInfo roomInfo = dynamoRepository.getRoomInfo(roomTypeId);
            return roomInfo;
    }
}
