package com.castis.publishservice.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        String requestUrl = response.request().url();
//        String requestPath = response.request().
        Response.Body responseBody = response.body();
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.is5xxServerError()) {
            return new FeignServerException(requestUrl, responseStatus, responseBody);
        } else if (responseStatus.is4xxClientError()) {
            return new FeignClientException(requestUrl, responseStatus, responseBody);
        } else {
            return new FeignException(requestUrl, responseStatus, "Generic exception", responseBody);
        }
    }
}
