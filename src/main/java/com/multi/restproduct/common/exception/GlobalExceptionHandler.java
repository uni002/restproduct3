package com.multi.restproduct.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ApiExceptionDto> exceptionHandler(RefreshTokenException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) //401
                .body(new ApiExceptionDto(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionDto> exceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .body(new ApiExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

}