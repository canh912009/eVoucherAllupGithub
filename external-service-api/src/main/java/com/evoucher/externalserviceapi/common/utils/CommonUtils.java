package com.evoucher.externalserviceapi.common.utils;

import com.evoucher.externalserviceapi.common.exception.CustomCodeException;
import com.evoucher.externalserviceapi.common.exception.GenericError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;

public class CommonUtils {

    public static HttpHeaders getJsonRequestResponseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static GenericError convertResponseError(HttpClientErrorException ex) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(ex.getResponseBodyAsString(), GenericError.class);
        } catch (JsonProcessingException e) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("external.message.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static GenericError convertResponseError(String jsonError) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonError, GenericError.class);
        } catch (JsonProcessingException e) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("external.message.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
