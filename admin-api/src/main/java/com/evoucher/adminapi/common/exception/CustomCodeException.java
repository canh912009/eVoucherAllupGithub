package com.evoucher.adminapi.common.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class CustomCodeException extends RuntimeException implements Serializable {
    private int code = 9999;
    private final HttpStatus status;

    public CustomCodeException(int code, String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public CustomCodeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static CustomCodeException internalException(Exception e) {
        return new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
