package com.kdu.ibebackend.exceptions;

import com.kdu.ibebackend.exceptions.custom.ValidParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Exception handler for all APIs
 */
@ControllerAdvice
@Slf4j
public class ApiException {
    @ExceptionHandler({ValidParamException.class})
    public ResponseEntity<String> paramsError(ValidParamException e) {
        log.error(e.getMessage());
        log.error(e.getClass().toGenericString());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<String> validationError(Exception e) {
        log.error(e.getMessage());
        log.error(e.getClass().toGenericString());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> error(Exception e) {
        log.error(e.getMessage());
        log.error(e.getClass().toGenericString());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
