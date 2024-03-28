package com.kdu.ibebackend.exceptions.custom;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom Exception for Invalid Promotion in API Requests
 */
@Slf4j
public class InvalidPromoException extends Exception {
    public InvalidPromoException(String errorMessage) {
        super(errorMessage);
        log.error(errorMessage);
    }
}
