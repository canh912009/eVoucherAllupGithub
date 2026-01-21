package com.evoucher.externalserviceapi.service.remote;

import com.evoucher.externalserviceapi.common.config.LoggedInClientContext;
import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.utils.CommonUtils;
import com.evoucher.externalserviceapi.service.model.request.ExternalPinUploadRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalPinRemoteService {

    @Value("${servers.admin-api}")
    private String adminApiPath;

    private final RestTemplate restTemplate;

    public Object findExternalPinUploadById(Integer id) throws ClientNotLoggedInException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpHeaders headers = CommonUtils.getJsonRequestResponseHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<Object> externalPinUpload =
                    restTemplate.exchange(
                            adminApiPath + "external-pin-upload/" + id,
                            HttpMethod.GET,
                            entity,
                            Object.class);
            return externalPinUpload.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("Find External PIN upload error: {}", ex.getMessage(), ex);
            return CommonUtils.convertResponseError(ex);
        }
    }

    public Object createExternalPinUpload(ExternalPinUploadRequest externalPinUploadRequest)
            throws ClientNotLoggedInException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpHeaders headers = CommonUtils.getJsonRequestResponseHeaders();
        headers.setBearerAuth(token);
        HttpEntity<ExternalPinUploadRequest> requestEntity =
                new HttpEntity<>(externalPinUploadRequest, headers);

        try {
            ResponseEntity<Object> externalPinUploadResponse =
                    restTemplate.exchange(
                            adminApiPath + "external-pin-upload",
                            HttpMethod.POST,
                            requestEntity,
                            Object.class);
            return externalPinUploadResponse.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("Find External PIN upload error: {}", ex.getMessage(), ex);
            return CommonUtils.convertResponseError(ex);
        }
    }

    public Object searchExternalPinUpload(Integer page, Integer pageSize, Integer goodsId)
            throws ClientNotLoggedInException {
        String token = LoggedInClientContext.loggedInClientToken();
        HttpHeaders headers = CommonUtils.getJsonRequestResponseHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<Object> externalPinUpload =
                    restTemplate.exchange(
                            adminApiPath + "external-pin-upload/search/" + page + "/" + pageSize + "?goodsId=" + goodsId,
                            HttpMethod.GET,
                            entity,
                            Object.class);

            return externalPinUpload.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("Find External PIN upload error: {}", ex.getMessage(), ex);
            return CommonUtils.convertResponseError(ex);
        }
    }
}
