package com.kdu.ibebackend.utils;

import com.kdu.ibebackend.service.SecretsManagerService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Initial Command line runner to execute functions before application starts running
 */
@Component
@Slf4j
public class InitRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("Application Initialized....");
    }
}