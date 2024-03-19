package com.kdu.ibebackend.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdu.ibebackend.models.TenantConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * Repository to perform CRUD Operations on DynamoDB
 */
@Repository
@Slf4j
public class TenantDynamoRepository {

    private final DynamoDBMapper dynamoDBMapper;

    @Autowired
    private Environment env;

    public TenantDynamoRepository(AmazonDynamoDB amazonDynamoDB) {
        this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    }

    @Cacheable("tenantconfig")
    public TenantConfig getTenantConfig(Number tenantId) throws JsonProcessingException {
        if(Objects.equals(env.getProperty("spring.profiles.active"), "dev") || Objects.equals(env.getProperty("spring.profiles.active"), "test")) {
            log.info(env.getProperty("config"));
            ObjectMapper mapper = new ObjectMapper();
            TenantConfig tenantConfig = mapper.readValue(env.getProperty("config"), TenantConfig.class);
            return tenantConfig;
        }
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
