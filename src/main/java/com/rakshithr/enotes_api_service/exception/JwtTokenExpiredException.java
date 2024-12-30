package com.rakshithr.enotes_api_service.exception;

public class JwtTokenExpiredException extends RuntimeException {

    public JwtTokenExpiredException(String message) {
        super(message);
    }

}
