package com.evoucher.evoucherbe.service.typed.system;

import com.evoucher.evoucherbe.dto.GoodDto;
import com.evoucher.evoucherbe.dto.VoucherDto;
import com.evoucher.evoucherbe.dto.VoucherExchangeReq;
import com.evoucher.evoucherbe.dto.partner_service.request.McpCardPurchaseRequest;
import com.evoucher.evoucherbe.dto.partner_service.request.McpTopUpRequest;
import com.evoucher.evoucherbe.exception.CustomCodeException;
import com.evoucher.evoucherbe.message.BaseResponse;
import com.evoucher.evoucherbe.message.DataResponse;
import com.evoucher.evoucherbe.utils.Constant;
import com.evoucher.evoucherbe.utils.ErrorCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

@Service("VNPT_EPAY")
@Slf4j
public class VnptService extends SystemAbstractService {
    public VnptService(SystemBridgeService bridgeService) {
        super(bridgeService);
    }

    @Override
    public BaseResponse processUsingVoucher(VoucherExchangeReq request, VoucherDto voucher, GoodDto good) throws CustomCodeException {
        log.info("using vnpt voucher: {}", request);

        BaseResponse response;

        switch (request.getVnptExchangeType()) {
            case TOPUP:
                response = topup(request.getVoucherId(), request.getVnptProviderCode(), request.getVnptReceiverPhoneNo());
                break;
            case CARDCODE:
                response = purchaseCard(request.getVoucherId(), request.getVnptProviderCode());
                if (response.isOk()) {
                    // in success case, update voucher expire date
                    Date expireDate = getExpireDateFromPin(response);
                    voucher.setExpirationDate(expireDate);
                }
                break;
            default:
                throw new CustomCodeException("vnpt exchange type" + request.getVnptExchangeType().name() + " is not excepted", ErrorCode.UNKNOWN_ERROR);
        }
        return response;
    }
    @SuppressWarnings("unchecked")
    public Date getExpireDateFromPin(BaseResponse response) throws CustomCodeException {
        DataResponse dataResponse = (DataResponse) response;
        Map<String, String> cardData = (Map<String, String>) dataResponse.getData();
        String dateString = cardData.get("expire");
        try {
            return DateUtils.parseDate(dateString, Constant.Common.COMMON_DATE_FORMAT);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException("error when parse data: ".concat(dateString), ErrorCode.UNKNOWN_ERROR);
        }
    }

    private BaseResponse topup(String ev, String provider, String receivePhone) throws CustomCodeException {
        try {
            McpTopUpRequest topup = new McpTopUpRequest();
            topup.setEv(ev);
            topup.setProvider(provider);
            topup.setTarget(receivePhone);

            log.info("request partner service with payload: {}", topup);
            BaseResponse response =  bridgeService.getPartnerServiceClient().topUpVnpt(topup);

            log.info("topup return: {}", response);
            return response;
        } catch (FeignException e) {
            log.error("Exception while processing topup Vnpt", e);
            throw new CustomCodeException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

    private DataResponse purchaseCard(String ev, String providerCode) {
        try {
            McpCardPurchaseRequest purchaseRequest = new McpCardPurchaseRequest();
            purchaseRequest.setEv(ev);
            purchaseRequest.setProvider(providerCode);

            log.info("request partner service purchase card with payload: {}", purchaseRequest);
            DataResponse response = bridgeService.getPartnerServiceClient().purchaseVnpt(purchaseRequest);

            log.info("card purchase return: {}", response);
            return response;
        } catch (FeignException e) {
            log.error("Exception while purchasing card Vnpt", e);
            throw new CustomCodeException(e.getMessage(), ErrorCode.UNKNOWN_ERROR);
        }
    }

}
