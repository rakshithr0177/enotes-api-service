package com.rakshithr.enotes_api_service.exception;

public class JwtAuthenticationException extends RuntimeException{

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
