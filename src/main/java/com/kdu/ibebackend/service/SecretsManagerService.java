package com.kdu.ibebackend.service;

import com.amazonaws.secretsmanager.caching.SecretCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public class SecretsManagerService {

    private final SecretCache secretCache;

    @Autowired
    public SecretsManagerService(SecretCache secretCache) {
        this.secretCache = secretCache;
    }

    public String getSecretValue(String secretName) {
        return secretCache.getSecretString(secretName);
    }
}
