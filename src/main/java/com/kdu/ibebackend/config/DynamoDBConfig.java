package com.kdu.ibebackend.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuration using DynamoDB in SpringBoot
 */
@Configuration
public class DynamoDBConfig {
    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.sessionKey}")
    private String sessionKey;
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        Regions region = Regions.AP_SOUTH_1;

        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicSessionCredentials(accessKey, secretKey, sessionKey));

        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(region)
                .withCredentials(credentialsProvider)
                .build();
    }
}