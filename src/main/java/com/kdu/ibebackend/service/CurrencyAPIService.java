package com.kdu.ibebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdu.ibebackend.dto.CurrencyAPIResponse;
import com.kdu.ibebackend.models.SecretConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyAPIService {
    private final SecretConfig secretConfig;
    private final WebClient webClient;

    @Autowired
    public CurrencyAPIService(SecretsManagerService secretsManagerService, @Value("${aws.secret.name}") String secretName) throws JsonProcessingException {
        String awsSecret = secretsManagerService.getSecretValue(secretName);
        ObjectMapper mapper = new ObjectMapper();
        secretConfig = mapper.readValue(awsSecret, SecretConfig.class);
        this.webClient = WebClient.create(secretConfig.getCurrencyApiUrl());
    }

    /**
     * Cache the Currency API data to avoid exhausting the Rate Limit
     * @return {@link com.kdu.ibebackend.dto.CurrencyAPIResponse Currency API Response} directly fetched from the API
     */
    @Cacheable("exchangerates")
    public ResponseEntity<CurrencyAPIResponse> executePostRequest() {
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
