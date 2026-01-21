package com.castis.publishservice.client;

import feign.Response;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeignServerException extends FeignException{
    public FeignServerException(String url, HttpStatus status, Response.Body body) {
        super(url, status, "Server Exception", body);
    }
}
