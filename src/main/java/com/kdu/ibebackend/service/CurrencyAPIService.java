package com.kdu.ibebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdu.ibebackend.dto.CurrencyAPIResponse;
import com.kdu.ibebackend.models.SecretConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@Slf4j
public class CurrencyAPIService {
    private final SecretConfig secretConfig;
    private final WebClient webClient;

    private Environment env;

    @Autowired
    public CurrencyAPIService(SecretsManagerService secretsManagerService, @Value("${awsSecretName}") String secretName, Environment env) throws JsonProcessingException {
        String awsSecret = secretsManagerService.getSecretValue(secretName);
        ObjectMapper mapper = new ObjectMapper();
        secretConfig = mapper.readValue(awsSecret, SecretConfig.class);
        this.webClient = WebClient.create(secretConfig.getCurrencyApiUrl());
        this.env = env;
    }

    /**
     * Cache the Currency API data to avoid exhausting the Rate Limit
     * @return {@link com.kdu.ibebackend.dto.CurrencyAPIResponse Currency API Response} directly fetched from the API
     */
    @Cacheable("exchangerates")
    public ResponseEntity<CurrencyAPIResponse> executePostRequest() throws JsonProcessingException {
        if(Objects.equals(env.getProperty("spring.profiles.active"), "dev")) {
            ObjectMapper mapper = new ObjectMapper();
            CurrencyAPIResponse response = mapper.readValue(env.getProperty("exhangerates"), CurrencyAPIResponse.class);
            log.info(response.toString());
            return ResponseEntity.ok(response);
        }

        CurrencyAPIResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/latest")
                        .queryParam("apikey", secretConfig.getCurrencyApiKey())
                        .build())
                .retrieve()
                .bodyToMono(CurrencyAPIResponse.class)
                .block();

        return ResponseEntity.ok(response);
    }

    /**
     * TTL of one day for the Currency API Data
     */
    @CacheEvict("exchangerates")
    @Scheduled(fixedRate = 86400000)
    public void evictDataCache() {
    }
}
