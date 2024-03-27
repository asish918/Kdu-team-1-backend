package com.kdu.ibebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.dto.CurrencyAPIResponse;
import com.kdu.ibebackend.models.TenantConfig;
import com.kdu.ibebackend.repository.DynamoRepository;
import com.kdu.ibebackend.service.CurrencyAPIService;
import com.kdu.ibebackend.utils.ParamUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.Min;
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
    private final DynamoRepository dynamoRepository;
    private final CurrencyAPIService currencyAPIService;

    @Autowired
    public ConfigController(
            DynamoRepository dynamoRepository, CurrencyAPIService currencyAPIService) {
        this.dynamoRepository = dynamoRepository;
        this.currencyAPIService = currencyAPIService;
    }

    @Operation(summary = "Config Operation",
            parameters = {
                    @Parameter(name = "X-Api-Key", description = "API Key", required = true, in = ParameterIn.HEADER)
            })
    @GetMapping("config")
    public TenantConfig testDynamo(@RequestParam @Min(1) String tenantId) throws Exception {
        if(!ParamUtils.isNumeric(tenantId)) throw new Exception("Tenant Id should be string");
        return dynamoRepository.getTenantConfig(Integer.parseInt(tenantId));
    }

    @Operation(summary = "Fetch Exchange Rates",
            parameters = {
                    @Parameter(name = "X-Api-Key", description = "API Key", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyAPIResponse.class))),
    })
    @GetMapping("/exchangerates")
    public ResponseEntity<CurrencyAPIResponse> exchangeRates() throws JsonProcessingException {
        return currencyAPIService.executePostRequest();
    }
}
