package com.kdu.ibebackend.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.secretsmanager.caching.SecretCache;
import com.amazonaws.secretsmanager.caching.SecretCacheConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.TimeUnit;

@Configuration
public class AwsSecretsManagerConfig {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.sessionKey}")
    private String sessionKey;

    @Bean
    public AWSSecretsManager awsSecretsManager() {
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(
                new BasicSessionCredentials(accessKey, secretKey, sessionKey));

        return AWSSecretsManagerClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(credentialsProvider)
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
