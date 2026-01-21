package com.castis.publishservice.client;

import feign.Response;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeignException extends RuntimeException{
    private String url;
    private HttpStatus status;
    private String originalMessage;
    private Response.Body body;
    public FeignException(String url, HttpStatus status, String message, Response.Body body) {
        super(message);
        this.url = url;
        this.status = status;
        this.originalMessage = message;
        this.body = body;
    }
}
