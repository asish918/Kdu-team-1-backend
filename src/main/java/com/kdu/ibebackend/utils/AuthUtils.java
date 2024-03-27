package com.kdu.ibebackend.utils;

import org.springframework.util.AntPathMatcher;

public class AuthUtils {
    public static Boolean validateSwaggerDocsPath(String URI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match("/swagger-ui/**", URI) || pathMatcher.match("/v3/api-docs/**", URI) || pathMatcher.match("/swagger-ui.html", URI) || pathMatcher.match("/webjars/swagger-ui/**", URI);
    }
}
