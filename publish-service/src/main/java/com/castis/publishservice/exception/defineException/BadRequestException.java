package com.castis.publishservice.exception.defineException;

public class BadRequestException extends RuntimeException{
    public BadRequestException(Exception ex) {
        super(ex.getMessage(), ex);
    }
    public BadRequestException(String message) {
        super(message);
    }
}
