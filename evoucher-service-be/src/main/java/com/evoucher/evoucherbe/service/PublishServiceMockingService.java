package com.evoucher.evoucherbe.service;

import com.evoucher.evoucherbe.common.enums.SystemType;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.exception.GenericError;
import com.evoucher.evoucherbe.service.typed.ServiceFactory;
import com.evoucher.evoucherbe.service.request.ExternalPinGiftPopRequest;
import com.evoucher.evoucherbe.utils.Constant;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublishServiceMockingService {
    private final ServiceFactory extServiceFactory;
    public List<Long> callAPIGet3rdPartyExternalPinId(Long goodsId, Integer quantity, SystemType type) throws CustomCodeException {
        try {
            log.info("Call api create external pin");
            ExternalPinGiftPopRequest payload = ExternalPinGiftPopRequest.builder()
                    .goodsId(goodsId)
                    .quantity(quantity)
                    .expireDate(new Date())
                    .build();

            log.info("Call publish-service with payload: {}", payload.toString());

            List<Long> response = extServiceFactory.getThirdPartyPinServiceByType(type).callPSToBuyThirdPartyPins(payload);
            if (CollectionUtils.isEmpty(response)) {
                throw new CustomCodeException(
                        "List Pin is empty",
                        400,
                        HttpStatus.BAD_REQUEST);
            }
            return response;


        } catch (HttpServerErrorException | HttpClientErrorException e) {
            log.error("Error response Publish service get External PIN: {}", e.getMessage(), e);
            String errorMessage = e.getMessage();
            int errorCode = 0;
            try {
                var error = Constant.gson.fromJson(e.getResponseBodyAsString(), GenericError.class);
                errorMessage = error.getMessage();
                errorCode = error.getCode();
            } catch (Exception ex) {
                // Ignore and do not need to be processed
            }
            throw new CustomCodeException(
                    errorMessage,
                    errorCode,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FeignException e) {
            log.error(e.contentUTF8());
            String errorMessage = e.getMessage();
            int errorCode = 0;
            try {
                var error = Constant.gson.fromJson(e.contentUTF8(), GenericError.class);
                errorMessage = error.getMessage();
                errorCode = error.getCode();
            } catch (Exception ex) {
                // Ignore and do not need to be processed
            }
            throw new CustomCodeException(
                    errorMessage,
                    errorCode,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            log.error("Error call Publish service get External PIN for {}: {}",type, e.getMessage(), e);
            throw new CustomCodeException(
                    "Call api publish service get pin for " +type.name()+ " error",
                    500,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
