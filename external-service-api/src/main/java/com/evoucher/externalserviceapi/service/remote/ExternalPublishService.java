package com.evoucher.externalserviceapi.service.remote;

import com.evoucher.externalserviceapi.common.config.LoggedInClientContext;
import com.evoucher.externalserviceapi.common.exception.ClientNotLoggedInException;
import com.evoucher.externalserviceapi.common.exception.CustomCodeException;
import com.evoucher.externalserviceapi.common.exception.GenericError;
import com.evoucher.externalserviceapi.common.message.BaseResponse;
import com.evoucher.externalserviceapi.common.utils.CommonUtils;
import com.evoucher.externalserviceapi.common.utils.ConstantUtils;
import com.evoucher.externalserviceapi.common.utils.DateUtils;
import com.evoucher.externalserviceapi.common.utils.MessageUtils;
import com.evoucher.externalserviceapi.entity.ExternalPublish;
import com.evoucher.externalserviceapi.repository.ExternalPublishRepository;
import com.evoucher.externalserviceapi.service.model.request.ExternalPublishConvert;
import com.evoucher.externalserviceapi.service.model.request.ExternalPublishRequest;
import com.evoucher.externalserviceapi.service.model.request.OrderPinRequest;
import com.evoucher.externalserviceapi.service.model.response.ExternalPublishOrderPinResponse;
import com.evoucher.externalserviceapi.service.model.response.ExternalPublishResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalPublishService {

    @Value("${servers.admin-api}")
    private String adminApiPath;

    private final ExternalPublishRepository externalPublishRepository;
    private final CloseableHttpClient httpClient;
    private final FileUploadService fileUploadService;


    public BaseResponse uploadExternalPublish(ExternalPublishRequest externalPublishRequest)
            throws IOException, ClientNotLoggedInException {
        log.info("Validate External publish request with transactionId: {}",
                externalPublishRequest.getTransactionId());
        validateExternalPublishRequest(externalPublishRequest);
        Date requestTime = DateUtils.getDateFromStringWithCommonFormat(externalPublishRequest.getRequestTime());
        Date smsScheduleDate = DateUtils.getDateFromStringWithCommonFormat(externalPublishRequest.getSmsSchedule());

        String contentImageName = null;
        String contentImagePath = null;
        MultipartFile contentImage = externalPublishRequest.getContentImage();
        if (ObjectUtils.isNotEmpty(contentImage)) {
            contentImageName = contentImage.getOriginalFilename();
            log.info("Save file: {}", contentImageName);
            contentImagePath = fileUploadService.uploadImageFile(contentImage);
        }


        log.info("Save External publish request with transactionId: {}",
                externalPublishRequest.getTransactionId());
        externalPublishRepository.save(ExternalPublish.builder()
                                    .transactionId(externalPublishRequest.getTransactionId().toString())
                                    .sha(externalPublishRequest.getSha())
                                    .requestTime(requestTime)
                                    .userId(externalPublishRequest.getUserId())
                                    .isSendSms(externalPublishRequest.getIsSendSms())
                                    .smsSchedule(smsScheduleDate)
                                    .subject(externalPublishRequest.getSubject())
                                    .contentText(externalPublishRequest.getContentText())
                                    .contentImagePath(contentImagePath)
                                    .contentImageName(contentImageName)
                                    .contentLink(externalPublishRequest.getContentLink())
                                    .orders(ConstantUtils.gson.toJson(externalPublishRequest.getOrders()))
                                    .build());

        ExternalPublishConvert requestEntity = ExternalPublishConvert.builder()
                                    .transactionId(externalPublishRequest.getTransactionId())
                                    .sha(externalPublishRequest.getSha())
                                    .requestTime(requestTime)
                                    .userId(externalPublishRequest.getUserId())
                                    .isSendSms(externalPublishRequest.getIsSendSms())
                                    .smsSchedule(smsScheduleDate)
                                    .subject(externalPublishRequest.getSubject())
                                    .contentText(externalPublishRequest.getContentText())
                                    .contentLink(externalPublishRequest.getContentLink())
                                    .orders(externalPublishRequest.getOrders())
                                    .build();
        String jsonRequest = ConstantUtils.gson.toJson(requestEntity);
        StringEntity entity = new StringEntity(jsonRequest);

        String token = LoggedInClientContext.loggedInClientToken();
        HttpPost httpPost = new HttpPost(adminApiPath + "partner/external-publish");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("Authorization", token);
        httpPost.setEntity(entity);

        log.info("Create external publish with request: {}", jsonRequest);
        HttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Publish response: {}", jsonResponse);

        if (statusCode == 200) {
            ExternalPublishResponse publishResponse = ConstantUtils.gson.fromJson(
                    jsonResponse, ExternalPublishResponse.class);
            return BaseResponse.builder()
                    .data(publishResponse)
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(),
                    HttpStatus.valueOf(statusCode));
        }
    }

    public BaseResponse checkOrderProgressByTransaction(UUID transactionId)
            throws ClientNotLoggedInException, IOException {
        String token = LoggedInClientContext.loggedInClientToken();
        String uriBuilder =
                UriComponentsBuilder.fromUriString(adminApiPath + "partner/external-publish")
                        .queryParam("transactionId", transactionId)
                        .build()
                        .toUriString();
        HttpGet httpGet = new HttpGet(uriBuilder);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Authorization", token);

        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("External publish response: {}", jsonResponse);

        if (statusCode == 200) {
            ExternalPublishResponse publishResponse = ConstantUtils.gson.fromJson(
                    jsonResponse, ExternalPublishResponse.class);
            return BaseResponse.builder()
                    .data(publishResponse)
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(),
                    HttpStatus.valueOf(statusCode));
        }
    }

    public BaseResponse checkOrderProgressByOrderId(Integer orderId)
            throws ClientNotLoggedInException, IOException {
        String token = LoggedInClientContext.loggedInClientToken();
        String uriBuilder =
                UriComponentsBuilder.fromUriString(adminApiPath + "partner/external-publish")
                        .queryParam("orderId", orderId)
                        .build()
                        .toUriString();
        HttpGet httpGet = new HttpGet(uriBuilder);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Authorization", token);

        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Order pin response: {}", jsonResponse);

        if (statusCode == 200) {
            ExternalPublishOrderPinResponse publishResponse = ConstantUtils.gson.fromJson(
                    jsonResponse, ExternalPublishOrderPinResponse.class);
            return BaseResponse.builder()
                    .data(publishResponse)
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(),
                    HttpStatus.valueOf(statusCode));
        }
    }

    public BaseResponse cancelExternalPublishByTransactionId(UUID transactionId)
            throws ClientNotLoggedInException, IOException {
        log.info("Check transaction already exist with transactionId: {}", transactionId);
        if (!externalPublishRepository.existsById(transactionId.toString())) {
            log.info("Transaction Id: {} not found", transactionId);
            throw new CustomCodeException(
                    MessageUtils.getMessage("external.message.transaction.id.not.found"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String token = LoggedInClientContext.loggedInClientToken();
        String uriBuilder =
                UriComponentsBuilder.fromUriString(adminApiPath + "partner/external-publish")
                        .queryParam("transactionId", transactionId)
                        .build()
                        .toUriString();
        HttpDelete httpDelete = new HttpDelete(uriBuilder);
        httpDelete.addHeader("Content-Type", "application/json");
        httpDelete.addHeader("Authorization", token);

        HttpResponse response = httpClient.execute(httpDelete);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Cancel External publish response: {}", jsonResponse);

        if (statusCode == 200) {
            return BaseResponse.builder()
                    .data(transactionId)
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(),
                    HttpStatus.valueOf(statusCode));
        }
    }

    public BaseResponse cancelOrderPinByOrderId(Integer orderId)
            throws ClientNotLoggedInException, IOException {
        String token = LoggedInClientContext.loggedInClientToken();
        String uriBuilder =
                UriComponentsBuilder.fromUriString(adminApiPath + "partner/external-publish")
                        .queryParam("orderId", orderId)
                        .build()
                        .toUriString();
        HttpDelete httpDelete = new HttpDelete(uriBuilder);
        httpDelete.addHeader("Content-Type", "application/json");
        httpDelete.addHeader("Authorization", token);

        HttpResponse response = httpClient.execute(httpDelete);

        int statusCode = response.getStatusLine().getStatusCode();
        String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");
        log.info("Cancel Order pin response: {}", jsonResponse);

        if (statusCode == 200) {
            return BaseResponse.builder()
                    .data(orderId)
                    .build();
        } else {
            GenericError genericError = CommonUtils.convertResponseError(jsonResponse);
            throw new CustomCodeException(genericError.getMessage(),
                    HttpStatus.valueOf(statusCode));
        }
    }

    private void validateExternalPublishRequest(ExternalPublishRequest externalPublishRequest) {
        if (ObjectUtils.isEmpty(externalPublishRequest)) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.external.publish.null"),
                    HttpStatus.BAD_REQUEST);
        }
        if (ObjectUtils.isEmpty(externalPublishRequest.getTransactionId())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.transaction.id.empty"),
                    HttpStatus.BAD_REQUEST);
        }
        if (ObjectUtils.isNotEmpty(externalPublishRequest.getSha())
                && externalPublishRequest.getSha().length() > 255) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.length.invalid", "SHA", "255"),
                    HttpStatus.BAD_REQUEST);
        }
        if (ObjectUtils.isEmpty(externalPublishRequest.getUserId())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.user.id.empty"),
                    HttpStatus.BAD_REQUEST);
        } else if ( externalPublishRequest.getSha().length() > 100) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.length.invalid", "UserId", "100"),
                    HttpStatus.BAD_REQUEST);
        }
        if (externalPublishRepository.existsById(externalPublishRequest.getTransactionId().toString())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.transaction.id.already.exist"),
                    HttpStatus.BAD_REQUEST);
        }
        if (ObjectUtils.isEmpty(externalPublishRequest.getRequestTime())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.request.time.empty"),
                    HttpStatus.BAD_REQUEST);
        } else  {
            // format request time
            DateUtils.getDateFromStringWithCommonFormat(externalPublishRequest.getRequestTime());
        }
        if (StringUtils.isBlank(externalPublishRequest.getSubject())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.subject.empty"),
                    HttpStatus.BAD_REQUEST);
        } else if (externalPublishRequest.getSubject().length() > 64){
            throw new CustomCodeException(MessageUtils.getMessage("external.message.length.invalid", "Subject", "64"),
                    HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(externalPublishRequest.getContentText())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.content.text.empty"),
                    HttpStatus.BAD_REQUEST);
        } else if (externalPublishRequest.getContentText().length() > 1000){
            throw new CustomCodeException(MessageUtils.getMessage("external.message.length.invalid", "Content text", "1000"),
                    HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(externalPublishRequest.getContentLink())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.content.link.empty"),
                    HttpStatus.BAD_REQUEST);
        } else if (externalPublishRequest.getContentLink().length() > 500){
            throw new CustomCodeException(MessageUtils.getMessage("external.message.length.invalid", "Content link", "500"),
                    HttpStatus.BAD_REQUEST);
        }
        if (CollectionUtils.isEmpty(externalPublishRequest.getOrders())) {
            throw new CustomCodeException(MessageUtils.getMessage("external.message.order.pin.empty"),
                    HttpStatus.BAD_REQUEST);
        }
        List<OrderPinRequest> orderPinRequests = externalPublishRequest.getOrders();
        orderPinRequests
                .forEach(
                        orderPinRequest -> {
                            if (ObjectUtils.isEmpty(orderPinRequest.getGoodsId())) {
                                throw new CustomCodeException(
                                        MessageUtils.getMessage("external.message.order.pin.field.empty", "goodsId"),
                                        HttpStatus.BAD_REQUEST);
                            }
                            if (ObjectUtils.isEmpty(orderPinRequest.getUserPhoneNo())) {
                                throw new CustomCodeException(
                                        MessageUtils.getMessage("external.message.order.pin.field.empty", "userPhoneNo"),
                                        HttpStatus.BAD_REQUEST);
                            }
                            if (ObjectUtils.isEmpty(orderPinRequest.getUserName())) {
                                throw new CustomCodeException(
                                        MessageUtils.getMessage("external.message.order.pin.field.empty", "userName"),
                                        HttpStatus.BAD_REQUEST);
                            }
                        });
        if (Boolean.TRUE.equals(externalPublishRequest.getIsSendSms())
                && ObjectUtils.isNotEmpty(externalPublishRequest.getSmsSchedule())) {
            Date currentDate = new Date();
            Date smsScheduleDate = DateUtils.getDateFromStringWithCommonFormat(externalPublishRequest.getSmsSchedule());
            if (smsScheduleDate.before(currentDate)) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("external.message.sms.schedule.not.before.current.date"),
                        HttpStatus.BAD_REQUEST);
            }
        }
        if (ObjectUtils.isNotEmpty(externalPublishRequest.getContentImage())) {
            String originalFileName = externalPublishRequest.getContentImage().getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            List<String> allowedImageExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "svg");

            if (!allowedImageExtensions.contains(fileExtension)) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("external.message.content.image.invalid"),
                        HttpStatus.BAD_REQUEST);
            }
        }
    }
}
