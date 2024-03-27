package com.kdu.ibebackend.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdu.ibebackend.models.dynamodb.RoomInfo;
import com.kdu.ibebackend.models.dynamodb.TenantConfig;
import com.kdu.ibebackend.utils.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

/**
 * Repository to perform CRUD Operations on DynamoDB
 */
@Repository
@Slf4j
public class DynamoRepository {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    private Environment env;

    public DynamoRepository(AmazonDynamoDB amazonDynamoDB) {
        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    }

    public TenantConfig getTenantConfig(Number tenantId) throws JsonProcessingException {
        if(EnvUtils.localEnvironmentCheck(env.getProperty("spring.profiles.active"))) {
            log.info(env.getProperty("config"));
            ObjectMapper mapper = new ObjectMapper();
            TenantConfig tenantConfig = mapper.readValue(env.getProperty("config"), TenantConfig.class);
            return tenantConfig;
        }
        return dynamoDBMapper.load(TenantConfig.class, tenantId);
    }

    public RoomInfo getRoomInfo(Number roomTypeId) {
        return dynamoDBMapper.load(RoomInfo.class, roomTypeId);
    }

//    public void saveTenantConfig(TenantConfig tenantConfig) {
//        dynamoDBMapper.save(tenantConfig);
//    }
}
