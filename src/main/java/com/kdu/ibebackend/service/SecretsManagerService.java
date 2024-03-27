package com.kdu.ibebackend.service;

import com.amazonaws.secretsmanager.caching.SecretCache;
import com.kdu.ibebackend.utils.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecretsManagerService {
    private final Environment env;

    private final SecretCache secretCache;

    @Autowired
    public SecretsManagerService(SecretCache secretCache, Environment env) {
        this.secretCache = secretCache;
        this.env = env;
    }

    public String getSecretValue(String secretName) {
        if(EnvUtils.localEnvironmentCheck(env.getProperty("spring.profiles.active"))) {
            log.info(env.getProperty("secrets"));
            return env.getProperty("secrets");
        }

        return secretCache.getSecretString(secretName);
    }
}
