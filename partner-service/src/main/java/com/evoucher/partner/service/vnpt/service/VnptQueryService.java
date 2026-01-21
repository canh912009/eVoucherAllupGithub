package com.evoucher.partner.service.vnpt.service;

import com.evoucher.partner.service.bean.dtos.ThirdPartyHistoryDto;
import com.evoucher.partner.service.common.Common;
import com.evoucher.partner.service.config.CryptoUtils;
import com.evoucher.partner.service.exception.CryptoException;
import com.evoucher.partner.service.exception.define_exception.VnptException;
import com.evoucher.partner.service.vnpt.bean.*;
import com.evoucher.partner.service.vnpt.bean.request.*;
import com.evoucher.partner.service.vnpt.bean.response.Card;
import com.evoucher.partner.service.vnpt.bean.response.CardListContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VnptQueryService {
    public static final int SUCCESS_CODE = 0;
    public static final int PENDING_CODE = 99;
    private static final int REQUEST_TIMEOUT = 30000;
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

    @Value("${partner.vnpt.partner.name}")
    private String partnerName;
    @Value("${partner.vnpt.card.key}")
    private String cardDecryptKey;
    @Value("${partner.vnpt.default.uri}")
    @Getter
    private String vnptUrl;

    private final ObjectMapper objectMapper;
    private final PrivateKey vnptPrivateKey;
    private final InterfacesSoapBindingStub vnptSoapService;

    public String sign(String plainText) throws CryptoException {
        try {
            return CryptoUtils.sign(plainText, vnptPrivateKey);
        } catch (CryptoException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }


    public Long queryBalance() throws VnptException {
        try {
            log.info("vnpt query balance: {}", partnerName);
            String digitalSign = this.sign(partnerName);
            QueryBalanceResult result = vnptSoapService.queryBalance(partnerName, digitalSign);
            log.info("vnpt return: {}", objectMapper.writeValueAsString(result));

            if (isSuccess(result.getErrorCode())) {
                log.info("balance available: {}", result.getBalance_avaiable());
                return result.getBalance_avaiable();
            } else {
                log.error("[{}] - {}", result.getErrorCode(), result.getMessage());
                throw new VnptException(result.getErrorCode(), result.getMessage());
            }

        } catch (VnptException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, Common.UNKNOWN_ERROR_MESSAGE);
        }
    }

    public long paymentCDV(PaymentCdvRequestQuery request, ThirdPartyHistoryDto history) throws VnptException {
        try {
            log.info("top up with request: {}", request);
            history.setRequestBody(objectMapper.writeValueAsString(request));

            PaymentCdvResult result = vnptSoapService.paymentCDV(
                    request.getRequestId(),
                    request.getPartnerName(),
                    request.getProvider(),
                    request.getType(),
                    request.getAccount(),
                    request.getAmount(),
                    request.getTimeout(),
                    request.getSign()
            );
            String responseString = objectMapper.writeValueAsString(result);
            log.info("vnpt return: {}", responseString);
            history.receiveResponse(responseString);

            if (isSuccess(result.getErrorCode())) {
                log.info("top up success: {}", result.getAmountTopupSuccess());
                history.ok();
                return result.getAmountTopupSuccess();
            } else {
                history.fail(result.getMessage());
                throw logAndReturnError(result.getErrorCode(), result.getMessage());
            }
        } catch (VnptException e) {
            history.fail(e.getMessage());
            throw e;
        } catch (Exception e) {
            history.fail(e.getMessage());
            log.error(e.getMessage(), e);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, Common.UNKNOWN_ERROR_MESSAGE);
        }
    }

    public List<Card> downloadSoftPin(DownloadSoftPinRequestQuery request, ThirdPartyHistoryDto history) throws VnptException {
        try {
            log.info("buy pin with request: {}", request);
            history.sendRequest(objectMapper.writeValueAsString(request));
            DownloadSoftpinResult response = vnptSoapService.downloadSoftpin(
                    request.getRequestId(),
                    request.getPartnerName(),
                    request.getProvider(),
                    request.getAmount(),
                    request.getQuantity(),
                    request.getSign()
            );
            String responseString = objectMapper.writeValueAsString(response);
            log.info("vnpt return pins: {}", responseString);

            history.receiveResponse(responseString);

            if (isSuccess(response.getErrorCode())) {
                log.info("download soft pin successfully: {}", response.getListCards());
                history.ok();
                return parseVnptCard(response.getListCards());
            } else {
                history.fail(response.getMessage());
                throw logAndReturnError(response.getErrorCode(), response.getMessage());
            }

        } catch (VnptException e) {
            history.fail(e.getMessage());
            throw e;
        } catch (Exception e) {
            history.fail(e.getMessage());
            log.error(e.getMessage(), e);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    public List<Card> reDownloadSoftPin(ReDownloadSoftPinRequestQuery request) {
        try {
            request.setPartnerName(partnerName);
            prepareDetail(request);

            DownloadSoftpinResult response = vnptSoapService.reDownloadSoftpin(
                    request.getRequestId(),
                    request.getPartnerName(),
                    request.getSign()
            );

            log.info(" re download vnpt return pins: {}", objectMapper.writeValueAsString(response));
            if (isSuccess(response.getErrorCode())) {
                log.info("download soft pin successfully: {}", response.getListCards());
                return parseVnptCard(response.getListCards());
            } else {
                throw logAndReturnError(response.getErrorCode(), response.getMessage());
            }

        } catch (VnptException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    public void topUp(TopUpRequestQuery request, ThirdPartyHistoryDto history) throws VnptException{
        log.info("topup : {}", request);
        try {
            history.setRequestBody(objectMapper.writeValueAsString(request));
            TopupResult response = vnptSoapService.topup(
                    request.getRequestId(),
                    request.getPartnerName(),
                    request.getProvider(),
                    request.getTarget(),
                    request.getAmount(),
                    request.getSign()
            );
            log.info(" topup vnpt return : {}", objectMapper.writeValueAsString(response));
            history.receiveResponse(objectMapper.writeValueAsString(response));
            if (isSuccess(response.getErrorCode())) {
                log.info("topup successfully");
                history.ok();
            } else {
                throw logAndReturnError(response.getErrorCode(), response.getMessage());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, e.getMessage());
        }

    }



    public List<Card> parseVnptCard(String encryptedCardInfo) throws CryptoException, VnptException {
        if (StringUtils.isNotEmpty(encryptedCardInfo)) {
            encryptedCardInfo = encryptedCardInfo.
                    replaceAll("\n", "");
        }
        String decryptedCard = CryptoUtils.triDescDecrypt(encryptedCardInfo, cardDecryptKey);
        log.info("card list decrypted: {}", decryptedCard);

        try {
            if (StringUtils.isNotEmpty(decryptedCard)) {
                CardListContainer cardContainer = objectMapper.readValue(decryptedCard, CardListContainer.class);
                return Optional.ofNullable(cardContainer.getListCards()).orElse(new ArrayList<>())
                        .stream().map(this::parseCardInfo)
                        .collect(Collectors.toList());
            } else {
                throw new VnptException(
                        Common.UNKNOWN_ERROR_CODE,
                        "vnpt return empty card"
                );
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    public void prepareData(VnptQueryBaseRequest request) {
        request.setPartnerName(partnerName);
        request.setRequestId(partnerName.concat("_").concat(request.getRequestId()));
    }

    public void prepareDetail(PaymentCdvRequestQuery request) throws CryptoException {
        request.setTimeout(REQUEST_TIMEOUT);
        String rawSign = request.getRequestId()
                .concat(partnerName)
                .concat(request.getProvider())
                .concat(request.getType().toString())
                .concat(request.getAccount())
                .concat(request.getAmount().toString())
                .concat(request.getTimeout().toString());
        request.setSign(sign(rawSign));
    }

    public void prepareDetail(DownloadSoftPinRequestQuery request) throws CryptoException {
        String rawSign =
                request.getRequestId()
                .concat(partnerName)
                .concat(request.getProvider())
                .concat(request.getAmount().toString())
                .concat(request.getQuantity().toString());
        request.setSign(sign(rawSign));
    }

    public void prepareDetail(ReDownloadSoftPinRequestQuery request) throws CryptoException {
        String rawString =
                request.getRequestId().concat(partnerName);
        request.setSign(sign(rawString));
    }

    public void prepareDetail(TopUpRequestQuery request) throws CryptoException {
        String rawString = request.getRequestId()
                .concat(request.getPartnerName())
                .concat(request.getProvider())
                .concat(request.getTarget())
                .concat(String.valueOf(request.getAmount()));
        request.setSign(sign(rawString));
    }


    private VnptException logAndReturnError(Integer errorCode, String message) {
        log.error("vnpt return error [{}] - {}", errorCode, message);
        return new VnptException(errorCode, message);
    }

    private Card parseCardInfo(String input) throws VnptException {
        log.info("parse input: {} to card", input);
        String[] parts = input.split("\\|");
        if (parts.length != 5) {
            log.error("input does not have enough 5 parts");
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, "Input string is not in the expected format: ".concat(input));
        }
        try {
            Card data = new Card();
            data.setProvider(parts[0]);
            data.setAmount(Integer.parseInt(parts[1]));
            data.setSerial(parts[2]);
            data.setPin(parts[3]);

            Date date = DEFAULT_DATE_FORMAT.parse(parts[4]);
            data.setExpire(date);
            log.info("parse card successfully");
            return data;
        } catch (ParseException e) {
            log.error("error when parse date: {}", parts[4]);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, e.getMessage());
        } catch (NumberFormatException e) {
            log.error("error when parse integer: {}", parts[1]);
            throw new VnptException(Common.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    public boolean isSuccess(Integer returnCode) {
        return returnCode == SUCCESS_CODE || returnCode == PENDING_CODE;
    }
}
