package com.kdu.ibebackend.service;

import com.kdu.ibebackend.constants.Constants;
import com.kdu.ibebackend.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

@Slf4j
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = Constants.AUTH_TOKEN_HEADER;

    private static String AUTH_TOKEN = Constants.AUTH_TOKEN;

    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            log.info(AUTH_TOKEN);
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}