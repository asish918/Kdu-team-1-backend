package com.kdu.ibebackend.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.secretsmanager.caching.SecretCache;
import com.amazonaws.secretsmanager.caching.SecretCacheConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class AwsSecretsManagerConfig {

    @Bean
    public AWSSecretsManager awsSecretsManager() {

        return AWSSecretsManagerClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .build();
    }

    @Bean
    public SecretCache secretCache(AWSSecretsManager awsSecretsManager) {
        SecretCacheConfiguration cacheConfig = new SecretCacheConfiguration()
                .withMaxCacheSize(10)
                .withCacheItemTTL(TimeUnit.MINUTES.toMillis(5))
                .withClient(awsSecretsManager);
        return new SecretCache(cacheConfig);
    }
}
