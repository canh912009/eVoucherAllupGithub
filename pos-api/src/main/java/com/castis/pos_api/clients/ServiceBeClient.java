package com.castis.pos_api.clients;

import com.castis.pos_api.dto.request.third_party.ServiceBeUsingVoucherReq;
import com.castis.pos_api.dto.response.third_party.ServiceBeBaseResponse;
import com.castis.pos_api.exception.ApplicationException;
import com.castis.pos_api.utils.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceBeClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${component.service-be.url}")
    private String serviceBeUrl;

    public ServiceBeBaseResponse requestUsingVoucherList(List<ServiceBeUsingVoucherReq> request) {
        try {
            log.info("Call EVoucher-service-BE api create child of choice voucher");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<List<ServiceBeUsingVoucherReq>> entity = new HttpEntity<>(request, headers);

            log.info("Call EVoucher-service-BE with payload: {}", request.toString());
            ResponseEntity<ServiceBeBaseResponse> response =
                    restTemplate.exchange(
                            serviceBeUrl + "/use-vouchers",
                            HttpMethod.POST,
                            entity,
                            ServiceBeBaseResponse.class);
            log.info("EVoucher-service-BE response: {}", response);

            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("Error when call service be for using voucher: {}", e.getMessage(), e);
            try {
                return objectMapper.convertValue(e.getResponseBodyAsString(), ServiceBeBaseResponse.class);
            } catch (IllegalArgumentException ex) {
                log.error(ex.getMessage());
                throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
                // Ignore and do not need to be processed
            }
        } catch (Exception e) {
            log.error("Error call EVoucher service BE create child of choice voucher: {}", e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }
    public ServiceBeBaseResponse requestUsingVoucher(ServiceBeUsingVoucherReq request) {
        try {
            log.info("Call EVoucher-service-BE api create child of choice voucher");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ServiceBeUsingVoucherReq> entity = new HttpEntity<>(request, headers);

            log.info("Call EVoucher-service-BE with payload: {}", request.toString());
            ResponseEntity<ServiceBeBaseResponse> response =
                    restTemplate.exchange(
                            serviceBeUrl + "/use-voucher",
                            HttpMethod.POST,
                            entity,
                            ServiceBeBaseResponse.class);
            log.info("EVoucher-service-BE response: {}", response);

            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("Error when call service be for using voucher: {}", e.getMessage(), e);
            try {
                return objectMapper.convertValue(e.getResponseBodyAsString(), ServiceBeBaseResponse.class);
            } catch (IllegalArgumentException ex) {
                log.error(ex.getMessage());
                throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
                // Ignore and do not need to be processed
            }
        } catch (Exception e) {
            log.error("Error call EVoucher service BE create child of choice voucher: {}", e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }

    public ServiceBeBaseResponse requestCancelVoucher(ServiceBeUsingVoucherReq request) {
        try {
            log.info("Call EVoucher-service-BE api create child of choice voucher");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<ServiceBeUsingVoucherReq> entity = new HttpEntity<>(request, headers);

            log.info("Call EVoucher-service-BE with payload: {}", request.toString());
            ResponseEntity<ServiceBeBaseResponse> response =
                    restTemplate.exchange(
                            serviceBeUrl + "/cancel-exchange",
                            HttpMethod.POST,
                            entity,
                            ServiceBeBaseResponse.class);
            log.info("EVoucher-service-BE response: {}", response);

            return response.getBody();
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("Error response EVoucher service BE create child of choice voucher: {}", e.getMessage(), e);
            try {
                return objectMapper.convertValue(e.getResponseBodyAsString(), ServiceBeBaseResponse.class);
            } catch (IllegalArgumentException ex) {
                log.error(ex.getMessage());
                throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
                // Ignore and do not need to be processed
            }
        } catch (Exception e) {
            log.error("Error call EVoucher service BE create child of choice voucher: {}", e.getMessage(), e);
            throw new ApplicationException(CustomResponse.E5001_INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        }
    }
}
