package com.evoucher.partner.service.exception.define_exception;

public class QuartzCreationException extends QuartzJobException{
    public QuartzCreationException(String message) {
        super(message);
    }

    public QuartzCreationException(String message, RuntimeException e) {
        super(message, e);
    }
}
