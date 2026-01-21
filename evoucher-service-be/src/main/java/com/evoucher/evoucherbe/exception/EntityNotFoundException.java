package com.evoucher.evoucherbe.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private Integer code;
    public EntityNotFoundException(String message) {
        super(message);
    }
    public EntityNotFoundException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
