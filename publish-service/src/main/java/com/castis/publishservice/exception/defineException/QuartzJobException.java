package com.castis.publishservice.exception.defineException;

public class QuartzJobException extends RuntimeException{
    public QuartzJobException(String message) {
        super(message);
    }
    public QuartzJobException(String message, RuntimeException e) {
        super(message, e);
    }
}
