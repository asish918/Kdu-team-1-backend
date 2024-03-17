package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.models.TenantConfig;
import com.kdu.ibebackend.repository.TenantDynamoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for Configuration APIs
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api")
@Slf4j
public class ConfigController {
    private final TenantDynamoRepository tenantDynamoRepository;

    @Autowired
    public ConfigController(
                          TenantDynamoRepository tenantDynamoRepository) {
        this.tenantDynamoRepository = tenantDynamoRepository;
    }

    @GetMapping("config/{tenantId}")
    public TenantConfig testDynamo(@PathVariable String tenantId) {
        return tenantDynamoRepository.getTenantConfig(Integer.parseInt(tenantId));
    }
}
