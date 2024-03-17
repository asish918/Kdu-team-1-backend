package com.kdu.ibebackend.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service Layer for interacting DynamoDB
 */
@Service
public class DynamoDBService {

    private final AmazonDynamoDB amazonDynamoDB;

    @Autowired
    public DynamoDBService(AmazonDynamoDB amazonDynamoDB) {
        this.amazonDynamoDB = amazonDynamoDB;
    }
}
