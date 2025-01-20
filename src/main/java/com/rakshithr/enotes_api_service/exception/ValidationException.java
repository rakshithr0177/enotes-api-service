package com.rakshithr.enotes_api_service.exception;

import java.util.Map;

public class ValidationException extends RuntimeException{

    private final Map<String, Object> error;

    public ValidationException(Map<String, Object> error) {
        super("Validation failed");
        this.error = error;
    }

    public Map<String, Object> getErrors(){
        return error;
    }
}
