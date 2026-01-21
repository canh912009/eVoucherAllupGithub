package com.castis.publishservice.exception.defineException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class ServerRuntimeException extends RuntimeException{
    Integer code;
    String message;
    public ServerRuntimeException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public ServerRuntimeException(String message) {
        super(message);
        this.message = message;
    }
    public ServerRuntimeException(String message, Exception ex) {
        super(message, ex);
        this.message = message;
    }
}
