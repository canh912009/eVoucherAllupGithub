package com.evoucher.externalserviceapi.service.remote;

import com.evoucher.externalserviceapi.common.config.LoggedInClientContext;
import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.exception.CustomCodeException;
import com.evoucher.externalserviceapi.common.utils.MessageUtils;
import com.evoucher.externalserviceapi.service.model.response.ImageFileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadService {

    @Value("${servers.file-upload-api}")
    private String fileUploadApi;

    private final RestTemplate restTemplate;


    public String uploadImageFile(MultipartFile file)
            throws ClientNotLoggedInException {

        long fileSizeInBytes = file.getSize();
        long maxSizeInBytes = 200 * 1024; // 200KB = 200 * 1024 bytes
        if (fileSizeInBytes > maxSizeInBytes) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("contentImage.size.exceeded.error"),
                    HttpStatus.BAD_REQUEST);
        }

        String token = LoggedInClientContext.loggedInClientToken();
        String uploadUrl = fileUploadApi + "/api/images/upload";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource resource = file.getResource();
        body.add("image", resource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            var response = restTemplate.postForEntity(uploadUrl, requestEntity, ImageFileResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("File uploaded successfully response: {}", response);
                ImageFileResponse imageResponse = response.getBody();
                return fileUploadApi + imageResponse.getPath();
            } else {
                log.info("File upload failed. HTTP Status Code: " + response.getStatusCode());
                return null;
            }
        } catch (HttpClientErrorException e) {
            log.error("Upload file image Error: {}", e.getMessage());
            throw new CustomCodeException(
                    MessageUtils.getMessage("external.message.upload.file.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
