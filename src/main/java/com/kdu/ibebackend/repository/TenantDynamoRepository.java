package com.kdu.ibebackend.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kdu.ibebackend.models.TenantConfig;
import org.springframework.stereotype.Repository;

/**
 * Repository to perform CRUD Operations on DynamoDB
 */
@Repository
public class TenantDynamoRepository {

    private final DynamoDBMapper dynamoDBMapper;

    public TenantDynamoRepository(AmazonDynamoDB amazonDynamoDB) {
        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    }

    public TenantConfig getTenantConfig(Number tenantId) {
        return dynamoDBMapper.load(TenantConfig.class, tenantId);
    }

//    public void saveTenantConfig(TenantConfig tenantConfig) {
//        dynamoDBMapper.save(tenantConfig);
//    }
}
