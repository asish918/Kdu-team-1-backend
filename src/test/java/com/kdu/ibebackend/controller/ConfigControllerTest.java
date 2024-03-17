package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.models.TenantConfig;
import com.kdu.ibebackend.repository.TenantDynamoRepository;
import com.kdu.ibebackend.service.CurrencyAPIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ConfigController.class)
@ComponentScan(value = "com.kdu.ibebackend.config")
@ActiveProfiles("test")
public class ConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TenantDynamoRepository tenantDynamoRepository;

    @MockBean
    private CurrencyAPIService currencyAPIService;

    @Test
    public void getTenantConfig() throws Exception {
        // Mocking the service method
        TenantConfig tenantConfig = new TenantConfig();
        tenantConfig.setTenantId(123);
        given(tenantDynamoRepository.getTenantConfig(anyInt())).willReturn(tenantConfig);

        // Performing GET request
        mockMvc.perform(get("/api/config/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tenantId").value(123));
    }
}