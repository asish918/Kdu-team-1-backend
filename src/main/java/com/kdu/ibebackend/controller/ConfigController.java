package com.kdu.ibebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.dto.CurrencyAPIResponse;
import com.kdu.ibebackend.models.TenantConfig;
import com.kdu.ibebackend.repository.TenantDynamoRepository;
import com.kdu.ibebackend.service.CurrencyAPIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for Configuration APIs
 */
@RestController
@RequestMapping(path = "/api")
@Slf4j
public class ConfigController {
    private final TenantDynamoRepository tenantDynamoRepository;
    private final CurrencyAPIService currencyAPIService;

    @Autowired
    public ConfigController(
                          TenantDynamoRepository tenantDynamoRepository, CurrencyAPIService currencyAPIService) {
        this.tenantDynamoRepository = tenantDynamoRepository;
        this.currencyAPIService = currencyAPIService;
    }

    @GetMapping("config/{tenantId}")
    public TenantConfig testDynamo(@PathVariable String tenantId) throws JsonProcessingException {
        return tenantDynamoRepository.getTenantConfig(Integer.parseInt(tenantId));
    }

    @GetMapping("/exchangerates")
    public ResponseEntity<CurrencyAPIResponse> exchangeRates() throws JsonProcessingException {
        return currencyAPIService.executePostRequest();
    }
}
