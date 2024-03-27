package com.kdu.ibebackend.exceptions.custom;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom Exception for Invalid Parameters in API Requests
 */
@Slf4j
public class ValidParamException extends Exception {
    public ValidParamException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage);
    }
}
