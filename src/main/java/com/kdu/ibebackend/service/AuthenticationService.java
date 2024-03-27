package com.kdu.ibebackend.service;

import com.kdu.ibebackend.constants.AuthConstants;
import com.kdu.ibebackend.constants.Errors;
import com.kdu.ibebackend.security.ApiKeyAuthentication;
import com.kdu.ibebackend.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

@Slf4j
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = AuthConstants.AUTH_TOKEN_HEADER;

    private static String AUTH_TOKEN = AuthConstants.AUTH_TOKEN;

    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            if (AuthUtils.validateSwaggerDocsPath(request.getRequestURI())) {
                return new ApiKeyAuthentication(AUTH_TOKEN, AuthorityUtils.NO_AUTHORITIES);
            }

            log.info(AUTH_TOKEN);
            log.info(request.getRequestURI());
            throw new BadCredentialsException(Errors.INVALID_API_KEY);
        }



        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}