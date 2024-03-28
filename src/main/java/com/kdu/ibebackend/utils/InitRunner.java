package com.kdu.ibebackend.utils;

import com.kdu.ibebackend.service.DynamoDBService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initial Command line runner to execute functions before application starts running
 */
@Component
@Slf4j
public class InitRunner implements CommandLineRunner {
    private DynamoDBService dynamoDBService;
    @Autowired
    public InitRunner(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    @Override
    public void run(String... args) throws Exception {
        RoomUtils.findRoomInfo(dynamoDBService);
        log.info("Application Initialized. Caches in place....");
    }
}