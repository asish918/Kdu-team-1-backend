package com.kdu.ibebackend.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SESConfig {
    @Bean
    public AmazonSimpleEmailService emailService() {
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.AP_SOUTH_1).build();
    }
}
