package com.kdu.ibebackend.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration using DynamoDB in SpringBoot
 */
@Configuration
public class DynamoDBConfig {
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        Regions region = Regions.AP_SOUTH_1;

        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(region)
                .build();
    }
}