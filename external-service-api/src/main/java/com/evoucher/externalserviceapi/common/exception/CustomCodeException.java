package com.evoucher.externalserviceapi.common.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class CustomCodeException extends RuntimeException implements Serializable {
    private HttpStatus status;

    public CustomCodeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
