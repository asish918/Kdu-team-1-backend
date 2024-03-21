package com.kdu.ibebackend.service;

import com.kdu.ibebackend.constants.Constants;
import com.kdu.ibebackend.security.ApiKeyAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AUTH;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.AntPathMatcher;

@Slf4j
public class AuthenticationService {
    private static final String AUTH_TOKEN_HEADER_NAME = Constants.AUTH_TOKEN_HEADER;

    private static String AUTH_TOKEN = Constants.AUTH_TOKEN;

    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(AUTH_TOKEN)) {
            AntPathMatcher pathMatcher = new AntPathMatcher();
            if (pathMatcher.match("/swagger-ui/**", request.getRequestURI()) || pathMatcher.match("/v3/api-docs/**", request.getRequestURI()) || pathMatcher.match("/swagger-ui.html", request.getRequestURI()) || pathMatcher.match("/webjars/swagger-ui/**", request.getRequestURI())) {
                return new ApiKeyAuthentication(AUTH_TOKEN, AuthorityUtils.NO_AUTHORITIES);
            }

            log.info(AUTH_TOKEN);
            log.info(request.getRequestURI());
            throw new BadCredentialsException("Invalid API Key");
        }



        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}