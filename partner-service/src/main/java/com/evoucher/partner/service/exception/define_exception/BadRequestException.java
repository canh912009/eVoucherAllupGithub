package com.evoucher.partner.service.exception.define_exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(Exception ex) {
        super(ex.getMessage(), ex);
    }
    public BadRequestException(String message) {
        super(message);
    }
}
