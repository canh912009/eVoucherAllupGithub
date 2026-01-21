package com.castis.publishservice.client;

import feign.Response;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;

//@Getter
public class FeignClientException  extends FeignException{
    public FeignClientException(String url, HttpStatus status, Response.Body body) {
        super(url, status, "Client Exception", body);
    }
}
