package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.common.client.PartnerServiceClient;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.message.ApiBaseResponse;
import com.evoucher.adminapi.common.message.ApiDataResponse;
import com.evoucher.adminapi.common.message.BaseResponse;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VnptEpayService {
    private final PartnerServiceClient partnerServiceClient;
    private static final int SUCCESS_CODE = 0;

    public static class ErrorCode {
    }

    public BaseResponse getProviderBalance(boolean isXpay) throws CustomCodeException {
        log.info(isXpay ?  "get vnptEpay balance" : "get Xpay balance");
        try {
            ApiDataResponse<Long> response = isXpay ? partnerServiceClient.getAvailableXpayBalance() : partnerServiceClient.getAvailableBalance();
            log.info("current balance response: {}", response);
            int returnedCode = Optional.ofNullable(response).map(ApiBaseResponse::getCode).orElse(Constant.UNKNOWN_ERROR);
            if (returnedCode == SUCCESS_CODE ) {
                return BaseResponse.ok(response.getData());
            } else {
                throw new CustomCodeException(
                        returnedCode,
                        Optional.ofNullable(response).map(ApiBaseResponse::getMessage).orElse(MessageUtils.getMessage(Constant.UNKNOWN_ERROR_MSG)),
                        HttpStatus.INTERNAL_SERVER_ERROR
                );
            }
        } catch (FeignException e) {
            log.error(e.getMessage());
            throw new CustomCodeException(
                    Constant.UNKNOWN_ERROR,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


}
