package com.evoucher.partner.service.vnpt.service;

import com.evoucher.partner.service.bean.dtos.ThirdPartyHistoryDto;
import com.evoucher.partner.service.bean.dtos.VnptProviderDto;
import com.evoucher.partner.service.bean.dtos.VnptVoucherExchangeHistoryDto;
import com.evoucher.partner.service.bean.dtos.VoucherDto;
import com.evoucher.partner.service.bean.enum_type.SystemType;
import com.evoucher.partner.service.bean.enum_type.VnptExchangeType;
import com.evoucher.partner.service.bean.enum_type.VoucherStatusCode;
import com.evoucher.partner.service.bean.request.TransactionRequest;
import com.evoucher.partner.service.bean.request.VnptPurchaseRequest;
import com.evoucher.partner.service.bean.request.VnptTopUpRequest;
import com.evoucher.partner.service.bean.response.BaseResponse;
import com.evoucher.partner.service.bean.response.DataResponse;
import com.evoucher.partner.service.common.Common;
import com.evoucher.partner.service.common.PropertyConverter;
import com.evoucher.partner.service.exception.define_exception.CustomCodeException;
import com.evoucher.partner.service.exception.define_exception.VnptException;
import com.evoucher.partner.service.mapper.VnptPurchaseMapper;
import com.evoucher.partner.service.service.ThirdPartyHistoryService;
import com.evoucher.partner.service.service.VnptProviderBasicService;
import com.evoucher.partner.service.service.VnptVoucherExchangeHistoryService;
import com.evoucher.partner.service.service.VoucherService;
import com.evoucher.partner.service.vnpt.bean.request.DownloadSoftPinRequestQuery;
import com.evoucher.partner.service.vnpt.bean.request.TopUpRequestQuery;
import com.evoucher.partner.service.vnpt.bean.response.Card;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class VnptService {
    private static final VnptPurchaseMapper VNPT_PURCHASE_MAPPER = VnptPurchaseMapper.INSTANCE;

    private final VnptQueryService vnptQueryService;
    private final ThirdPartyHistoryService thirdPartyLoggingService;
    private final VnptProviderBasicService providerService;
    private final VnptVoucherExchangeHistoryService vnptExchangeHistoryService;
    private final VoucherService voucherService;
    private final ObjectMapper objectMapper;

    private final PropertyConverter propertyConverter;
    private static final Map<String, String> ACCEPT_MOBILE_NUMBER_PREFIX = new HashMap<>();

    static  {
        ACCEPT_MOBILE_NUMBER_PREFIX.put("+84", "\\+84");
        ACCEPT_MOBILE_NUMBER_PREFIX.put("0084", "0084");
        ACCEPT_MOBILE_NUMBER_PREFIX.put("0", "0");
    }

    private static class ErrorCode {
        private ErrorCode(){}
        public static final int PROVIDER_NOT_SUPPORT = 20201;
        public static final int RECEIVER_NULL = 202;
        public static final int INVALID_TARGET_NUMBER = 203;
        public static final int FACE_VALUE_NOT_SUPPORTED = 204;
    }


    public BaseResponse getAvailableBalance() throws VnptException {
        log.info("query vnpt available balance");
        return new DataResponse<>(vnptQueryService.queryBalance());
    }

    public DataResponse<Card> purchase(VnptPurchaseRequest request) throws VnptException {
        // validate
        validateTransactionRequest(request);

        log.info("purchase vnpt pin : {}", request);
        // create unique random request id
        request.setRequestId(UUID.randomUUID().toString());

        DownloadSoftPinRequestQuery vnptRequest = VNPT_PURCHASE_MAPPER
                .toVnptDownloadSoftPinRequest(request);
        // currently only by one card for each voucher
        vnptRequest.setQuantity(1);

        //map to vnpt request
        vnptQueryService.prepareData(vnptRequest);
        vnptQueryService.prepareDetail(vnptRequest);

        ThirdPartyHistoryDto history = thirdPartyLoggingService.makeNewOutBound(
                vnptQueryService.getVnptUrl(),
                SystemType.VNPT_EPAY
        );
        VnptVoucherExchangeHistoryDto purchaseHistory = vnptExchangeHistoryService.fromExchangeRequest(
                vnptRequest,
                request.getEv(),
                VnptExchangeType.CARDCODE
        );

        log.info("make new vnpt voucher purchase history: {}", purchaseHistory);
        try {
            List<Card> cardList = vnptQueryService.downloadSoftPin(vnptRequest, history);
            Card card = cardList.get(0);
            log.info("save card info to history: {}", objectMapper.writeValueAsString(card)) ;

            vnptExchangeHistoryService.updateCardInfoToHistory(purchaseHistory, card);

            log.info("set purchase history result to success");
            purchaseHistory.ok();
            return new DataResponse<>(cardList.get(0));
        } catch (VnptException e) {
            log.error("error when call to vnpt");
            purchaseHistory.fail();
            throw e;
        } catch (CustomCodeException e) {
            log.error(e.getMessage());
            purchaseHistory.fail();
            history.fail(e.getMessage());
            throw new VnptException(
                    Common.SERVER_ERROR,
                    e.getMessage()
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            purchaseHistory.fail();
            history.fail(e.getMessage());
            throw VnptException.vnptUnknownException(e);
        }
        finally {
            saveHistoryThenSavePurchaseHistory(history, purchaseHistory);
        }
    }

    /**
     * save third party history, then save party history id to vnpt voucher purchase history and save it
     * @param partyHistory third party history
     * @param exchangeHistory vnpt voucher exchange history
     */
    public void saveHistoryThenSavePurchaseHistory(ThirdPartyHistoryDto partyHistory, VnptVoucherExchangeHistoryDto exchangeHistory)
     throws CustomCodeException {
        partyHistory = thirdPartyLoggingService.save(partyHistory);
        log.info("save third party log: {}", partyHistory.getId());
        exchangeHistory.setRequestHistoryId(partyHistory.getId());
        exchangeHistory.setExchangeDate(new Date());

        exchangeHistory = vnptExchangeHistoryService.saveDto(exchangeHistory);
        log.info("save vnpt voucher purchase history: {}", exchangeHistory.getId());
    }

    public BaseResponse topup(VnptTopUpRequest request) throws VnptException {
        log.info("topup: {}", request);

        validateTransactionRequest(request);
        request.setRequestId(UUID.randomUUID().toString());

        TopUpRequestQuery topupRequest = VNPT_PURCHASE_MAPPER.toTopUpRequest(request);
        vnptQueryService.prepareData(topupRequest);
        // remove region code if needed
        validateMobileNumberPrefix(topupRequest.getTarget());
        replaceRegionCodeWithZero(topupRequest);
        vnptQueryService.prepareDetail(topupRequest);

        ThirdPartyHistoryDto history = thirdPartyLoggingService.makeNewOutBound(
                vnptQueryService.getVnptUrl(),
                SystemType.VNPT_EPAY
        );

        VnptVoucherExchangeHistoryDto purchaseHistory = vnptExchangeHistoryService.fromExchangeRequest(
                topupRequest,
                request.getEv(),
                VnptExchangeType.TOPUP
        );
        purchaseHistory.setTargetPhone(request.getTarget());
        try {
            vnptQueryService.topUp(topupRequest, history);
            vnptExchangeHistoryService.updateCardInfoToHistory(purchaseHistory, topupRequest);
            purchaseHistory.ok();
            return new BaseResponse();
        } catch (VnptException e) {
            log.error("error when call to vnpt");
            purchaseHistory.fail();
            history.fail(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            history.fail(e.getMessage());
            purchaseHistory.fail();
            throw VnptException.vnptUnknownException(e);
        }
        finally {
            saveHistoryThenSavePurchaseHistory(history, purchaseHistory);
        }
    }

    public static void replaceRegionCodeWithZero(TopUpRequestQuery request) {
        String target = request.getTarget();
        for (String validPrefix : ACCEPT_MOBILE_NUMBER_PREFIX.keySet()) {
            if (target.startsWith(validPrefix)) {
                log.info("target contain region code. replace with 0");
                String needToReplace = ACCEPT_MOBILE_NUMBER_PREFIX.get(validPrefix);
                String startWithZero = "0".concat(target.replaceFirst(needToReplace, ""));
                request.setTarget(startWithZero);
            }
        }
    }

    public static void validateMobileNumberPrefix(String number) throws VnptException {
        for (String validPrefix : ACCEPT_MOBILE_NUMBER_PREFIX.keySet()) {
            if (number.startsWith(validPrefix)) {
                log.info("number start with: {}", validPrefix);
                return;
            }
        }

        log.error("target number is invalid");
        throw new VnptException(
                ErrorCode.INVALID_TARGET_NUMBER,
                "target number is invalid"
        );
    }


    private void validateTransactionRequest(TransactionRequest request) throws CustomCodeException {
        try {
            VoucherDto voucher = voucherService.findDtoById(request.getEv());
            voucherService.validateExpire(voucher);
            voucherService.validateExistAndExpire(voucher, Set.of(VoucherStatusCode.NORMAL, VoucherStatusCode.PART_USED));
            voucherService.validateBalance(voucher, voucher.getGoods().getSellPrice().longValue());


            Long countValidProvider = voucherService.countVoucherByEvAndValidProviderCode(request.getEv(), request.getProvider());
            if (countValidProvider == 0L) {
                log.error("provider doesn't support");
                throw new CustomCodeException(ErrorCode.PROVIDER_NOT_SUPPORT, "provider not support");
            }
            VnptProviderDto providerDto = providerService.getValidProvider(request.getProvider());

            request.setFaceValue(voucher.getGoods().getListPrice().intValue());
            request.setSellPrice(voucher.getGoods().getSellPrice().longValue());

            List<Integer> supportedValues = new ArrayList<>();

            if (StringUtils.isNotBlank(providerDto.getAllowedCardFaces())) {
                TypeReference<List<Integer>> typeRef = new TypeReference<List<Integer>>() {};
                supportedValues = objectMapper.readValue(providerDto.getAllowedCardFaces(), typeRef);
            }

            if (!supportedValues.contains(request.getFaceValue())) {
                log.error("request value {} is not supported by provider: {} that only supports {}", request.getFaceValue(), providerDto.getProviderCd(), supportedValues);
                throw new VnptException(ErrorCode.FACE_VALUE_NOT_SUPPORTED, "Face value is not supported by provider");
            }

            if (request instanceof VnptTopUpRequest) {
                // some providers have topup code different with card purchase code
                request.setProvider(providerDto.getTopupProviderCd());
                String decrypted = propertyConverter.convertToEntityAttribute(((VnptTopUpRequest) request).getTarget());

                if (decrypted == null) {
                    log.info("target is null");
                    throw new VnptException(
                            ErrorCode.RECEIVER_NULL,
                            "Receiver mobile number is null"
                    );
                }
                ((VnptTopUpRequest) request)
                        .setTarget(decrypted);
            }
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw new CustomCodeException(VoucherService.ErrorCode.NOT_FOUND, e.getMessage());
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(Common.UNKNOWN_ERROR_CODE, "Error when parsing allow face value of vnpt provider");
        }
    }


}
