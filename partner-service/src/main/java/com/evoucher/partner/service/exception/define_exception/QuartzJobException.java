package com.evoucher.partner.service.exception.define_exception;

public class QuartzJobException extends RuntimeException{
    public QuartzJobException(String message) {
        super(message);
    }
    public QuartzJobException(String message, RuntimeException e) {
        super(message, e);
    }
}
