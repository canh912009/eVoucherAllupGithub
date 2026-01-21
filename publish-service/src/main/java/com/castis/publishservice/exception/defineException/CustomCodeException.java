package com.castis.publishservice.exception.defineException;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class CustomCodeException extends RuntimeException implements Serializable {
    private HttpStatus status;
    private int errorCode;
    private String detailMessage;

    public CustomCodeException(String message, HttpStatus status) {
        super(message);
        this.status = status;

    }
    public CustomCodeException(String message, HttpStatus status, String detailMessage) {
        super(message);
        this.status = status;
        this.detailMessage = detailMessage;
    }

    public CustomCodeException(String message, int errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
