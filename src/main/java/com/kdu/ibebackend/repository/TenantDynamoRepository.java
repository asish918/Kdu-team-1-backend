package com.kdu.ibebackend.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kdu.ibebackend.models.TenantConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Cacheable("tenantconfig")
    public TenantConfig getTenantConfig(Number tenantId) {
        return dynamoDBMapper.load(TenantConfig.class, tenantId);
    }

    @CacheEvict("tenantconfig")
    @Scheduled(fixedRate = 86400000)
    public void evictDataCache() {
    }

//    public void saveTenantConfig(TenantConfig tenantConfig) {
//        dynamoDBMapper.save(tenantConfig);
//    }
}
