package com.evoucher.evoucherbe.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class CustomCodeException extends RuntimeException implements Serializable {
    private HttpStatus status;
    private int errorCode;

    public CustomCodeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public CustomCodeException(String message, int code) {
        super(message);
        this.errorCode = code;
    }

    public CustomCodeException(String message, int errorCode, HttpStatus status) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}
