package com.castis.publishservice.service.external_pin.watane;

import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.request.watane.*;
import com.castis.publishservice.dto.response.watane.WataneBaseResponse;
import com.castis.publishservice.dto.response.watane.WataneProduct;
import com.castis.publishservice.dto.response.watane.WataneProductWrapper;
import com.castis.publishservice.dto.response.watane.WataneTransactionResult;
import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.entity.ExternalPinUpload;
import com.castis.publishservice.entity.ThirdPartyCallAPIHistory;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.repository.ExtPinRepository;
import com.castis.publishservice.repository.ExternalPinUploadRepository;
import com.castis.publishservice.repository.ThirdPartyCallAPIHistoryRepository;
import com.castis.publishservice.service.ThirdPartyCallAPIHistoryService;
import com.castis.publishservice.service.common.LockingService;
import com.castis.publishservice.service.external_pin.IntegratedPinService;
import com.castis.publishservice.utils.DateUtils;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import com.castis.publishservice.utils.enum_template.PinDisplayType;
import com.castis.publishservice.utils.enum_template.SystemType;
import com.castis.publishservice.utils.enum_template.ThirdPartyAction;
import com.castis.publishservice.utils.status.ExternalPinUploadStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service("WATANE")
@Slf4j
public class WataneService implements IntegratedPinService {
    private static final Integer MAX_QUANTITY_PER_REQUEST = 1;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static final String AQUA_PARTNER_NAME = "AQUA";
    public static final int ONE_ITEM_AT_A_TIME = 1;
    public static final String TRACKING_KEY = "transactionId";
    public static final String WATANE_MESSAGE = "Bạn đã nhận được voucher từ aQuà!";
    public static final String SYSTEM_REG_ACCOUNT = "System";
    public static final int WATANE_TRANSACTION_FAIL = 2;
    public static final int WATANE_TRANSACTION_SUCCESS = 1;
    private final ThirdPartyCallAPIHistoryRepository thirdPartyCallAPIHistoryRepository;

    @Value("${watane.appId}")
    private String appId;
    @Value("${watane.api-key}")
    private String apiKey;
    @Value("${watane.secret-key}")
    private String secretKey;
    @Value("${watane.url}")
    private String wataneUrl;

    private final RestTemplate restTemplate;
    private final ExternalPinUploadRepository externalPinUploadRepository;
    private final ExtPinRepository extPinRepository;
    private final LockingService lockingService;
    private final ThirdPartyCallAPIHistoryService thirdPartyCallAPIHistoryService;
    private final WataneSignatureService signatureService;

    public List<ExtPin> orderWatanePin(int pinsQuantity, GoodsDTO goods) {

        List<ExtPin> generatedPins = new ArrayList<>();

        while (pinsQuantity > 0) {
            int pinsQuantityWillGenerateInTime = Math.min(pinsQuantity, MAX_QUANTITY_PER_REQUEST);

            String orderNo = generateUniqueOrderNo();

            WatanePurchaseRequestBody requestBody = WatanePurchaseRequestBody.builder()
                    .requestTime(getRequestTime())
                    .transactionId(orderNo)
                    .message(WATANE_MESSAGE)
                    .additionalInfo(List.of(AdditionalInfo.builder()
                            .key(TRACKING_KEY)
                            .value(orderNo)
                            .build()))
                    .code(goods.getSupplierGoodsId())
                    .quantity(ONE_ITEM_AT_A_TIME)
                    .build();

            var logEntity = thirdPartyCallAPIHistoryService.makeNewOutBound(null, SystemType.WATANE);
            logEntity.setGoodsId(goods.getId());
            WataneBaseResponse<WataneProductWrapper> response = purchaseWataneProduct(requestBody, logEntity);
            // Only return 1 item
            WataneProduct product = response.getData().getCodes().get(0);

            ExtPin savedExtPin = saveExtPin(logEntity, product);
            generatedPins.add(savedExtPin);

            log.info("Update call API log: {}", logEntity.getId());
            logEntity.setUploadId(savedExtPin.getUploadId());
            thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity);

            pinsQuantity -= pinsQuantityWillGenerateInTime;
        }

        return generatedPins;
    }

    private String generateUniqueOrderNo() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder(12);
        stringBuilder.append(AQUA_PARTNER_NAME);

        for (int i = 0; i < 12; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }


    @Override
    public void reservePin(Integer userSize, GoodsDTO goods, Date bookingDate) {
        log.info("Watane type doesn't need to reserve pin");
    }

    @Override
    public List<ExtPin> getAvailablePin(int totalSize, GoodsDTO goods, Date bookingDate) {

        List<ExtPin> existPin = lockingService.getPins(goods.getId(), ExtPinStatus.AVAILABLE, totalSize, new Date());
        try {
            int numberPinMissing = totalSize - existPin.size();
            log.info("Order more Pin from Watane for goodsId: {} with number: {}",
                    goods.getId(), numberPinMissing);
            if (numberPinMissing > 0) {
                List<ExtPin> orderedPins;
                log.info("call Watane to order missing external pin");
                orderedPins = orderWatanePin(numberPinMissing, goods);
                existPin.addAll(orderedPins);
            }
            return existPin;
        } catch (CustomCodeException e) {
            log.error(String.format("Error: %s", e.getErrorCode()), e);
            //release pin
            lockingService.removePrcDonePins(goods.getId(), existPin.stream().map(ExtPin::getId).collect(Collectors.toList()));
            throw e;
        } catch (Exception e) {
            log.error("Error get pin for Watane: {}", e.getMessage(), e);
            //release pin
            lockingService.removePrcDonePins(goods.getId(), existPin.stream().map(ExtPin::getId).collect(Collectors.toList()));
            throw new CustomCodeException(
                    "Error get pin for Watane with goodsId: " + goods.getId(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ExtPin> getReservedPin(int totalSize, GoodsDTO goods, Date bookingDate) {
        return this.getAvailablePin(totalSize, goods, bookingDate);
    }

    private WataneBaseResponse<WataneProductWrapper> purchaseWataneProduct(WatanePurchaseRequestBody body, ThirdPartyCallAPIHistory logEntity) throws
            CustomCodeException {
        String urlCreateWataneVoucher = wataneUrl + "/giftstore/api/products/buy/direct";

        logEntity.setRequestUrl(urlCreateWataneVoucher);
        logEntity.setTransactionId(body.getTransactionId());
        logEntity.setAction(ThirdPartyAction.PURCHASE.name());
        try {
            WataneRequestHeader headerData = makeHeader(body);
            log.info("Watane purchase body: {}", objectMapper.writeValueAsString(body));
            log.info("Watane purchase header: {}", objectMapper.writeValueAsString(headerData));

            logEntity.setRequestBody(objectMapper.writeValueAsString(body));
            HttpHeaders headers = getHttpHeaders(headerData);
            HttpEntity<WatanePurchaseRequestBody> entity = new HttpEntity<>(body, headers);

            ResponseEntity<WataneBaseResponse<WataneProductWrapper>> response = restTemplate
                    .exchange(urlCreateWataneVoucher,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {
                            });

            String responseString = objectMapper.writeValueAsString(response.getBody());
            logEntity.setResponseBody(responseString);
            logEntity.setResponseTime(new Date());
            logEntity.setResult(getWataneRequestResult(response));
            log.info("Watane purchase response: {}", responseString);
            validateWataneResponse(response);

            return response.getBody();
        } catch (CustomCodeException e) {
            logEntity.setDescription(e.getMessage());
            log.error("Watane creation error: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logEntity.setResponseBody(e.getMessage());
            logEntity.setResponseTime(new Date());
            logEntity.setDescription(e.getMessage());
            log.error("Call api create Watane Voucher error: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    WataneErrorCode.GENERAL_EXCEPTION.getMessage(),
                    WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.info("Save log call api");
            thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity);
        }
    }

    private WataneProduct getTransactionStatus(WataneTransactionRequestBody body, ThirdPartyCallAPIHistory logEntity) throws
            CustomCodeException {
        String urlCheckTransactionStatus = wataneUrl + "/giftstore/api/transactions/status";

        try {
            logEntity.setRequestUrl(urlCheckTransactionStatus);
            logEntity.setTransactionId(body.getTransactionId());
            logEntity.setAction(ThirdPartyAction.CHECK_TRANS.name());

            WataneRequestHeader headerData = makeHeader(body);
            log.info("Watane checktrans body: {}", objectMapper.writeValueAsString(body));
            log.info("Watane checktrans header: {}", objectMapper.writeValueAsString(headerData));

            logEntity.setRequestBody(objectMapper.writeValueAsString(body));
            HttpHeaders headers = getHttpHeaders(headerData);
            HttpEntity<WataneTransactionRequestBody> entity = new HttpEntity<>(body, headers);

            ResponseEntity<WataneBaseResponse<WataneTransactionResult>> response = restTemplate
                    .exchange(urlCheckTransactionStatus,
                            HttpMethod.POST,
                            entity,
                            new ParameterizedTypeReference<>() {
                            });

            String responseString = objectMapper.writeValueAsString(response.getBody());
            logEntity.setResponseBody(responseString);
            logEntity.setResponseTime(new Date());
            logEntity.setResult(getWataneTransactionResult(response).name());
            log.info("Watane transaction response: {}", responseString);
            validateWataneTransactionResponse(response);

            return response.getBody().getData().getCodes().stream().findFirst().get();
        } catch (Exception e) {
            logEntity.setResponseBody(e.getMessage());
            logEntity.setResponseTime(new Date());
            logEntity.setDescription(e.getMessage());
            log.error("Call api create Watane Voucher error: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    WataneErrorCode.GENERAL_EXCEPTION.getMessage(),
                    WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.info("Save log call api");
            thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity);
        }
    }

    private static @NotNull Watane3rdPartyResult getWataneTransactionResult(ResponseEntity<WataneBaseResponse<WataneTransactionResult>> response) {
        if (Objects.isNull(response)
                ||Objects.isNull(response.getBody())
                || Boolean.FALSE.equals(response.getBody().getSuccess())
                || Objects.isNull(response.getBody().getData())
                || response.getBody().getData().getTransactionStatus() == WATANE_TRANSACTION_FAIL
                || Objects.isNull(response.getBody().getData().getCodes())
                || response.getBody().getData().getCodes().isEmpty()) {
            return Watane3rdPartyResult.FAIL;
        }
        return Watane3rdPartyResult.SUCCESS;
    }

    private static @NotNull String getWataneRequestResult(ResponseEntity<WataneBaseResponse<WataneProductWrapper>> response) {
        if (Objects.isNull(response.getBody())) {
            log.error("Watane response body is null");
            return Watane3rdPartyResult.FAIL.toString();
        }
        if (Boolean.TRUE.equals(response.getBody().getSuccess())) {
            return Watane3rdPartyResult.SUCCESS.toString();
        }
        WataneErrorCode wataneErrorCode = WataneErrorCode.fromWataneCode(response.getBody().getCode());
        if (EnumSet.of(WataneErrorCode.TRANSACTION_TIMEOUT, WataneErrorCode.TRANSACTION_IN_PROGRESS).contains(wataneErrorCode)) {
            log.warn("Transaction is in progress or timeout");
            return Watane3rdPartyResult.IN_PROGRESS.toString();
        }
        return Watane3rdPartyResult.FAIL.toString();
    }

    private static @NotNull HttpHeaders getHttpHeaders(WataneRequestHeader headerData) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("appId", headerData.getAppId());
        headers.add("signature", headerData.getSignature());
        headers.add("api-key", headerData.getApiKey());
        headers.add("token", headerData.getToken());
        return headers;
    }

    private WataneRequestHeader makeHeader(WataneRequestBody body) throws JsonProcessingException {
        String signature = objectMapper.writeValueAsString(body);
        String sign = signatureService.sign(signature);
        return WataneRequestHeader.builder()
                .appId(appId)
                .signature(sign)
                .apiKey(apiKey)
                .token(generateToken())
                .build();
    }

    private String generateToken() {
        String input = apiKey + secretKey;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomCodeException(
                    WataneErrorCode.ERROR_WHILE_CREATING_REQUEST.getMessage(),
                    WataneErrorCode.ERROR_WHILE_CREATING_REQUEST.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Hash the input and get the byte array
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        // Encode the byte array to Base64 to make it easier to use in requests
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void validateWataneResponse(ResponseEntity<WataneBaseResponse<WataneProductWrapper>> response) {
        if (Objects.isNull(response.getBody())) {
            throw new CustomCodeException(
                    WataneErrorCode.GENERAL_EXCEPTION.getMessage(),
                    WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (Objects.isNull(response.getBody().getSuccess()) || Boolean.FALSE.equals(response.getBody().getSuccess())) {
            log.error("Error response from Watane: {}", response.getBody().getCode());
            WataneErrorCode errorCode = WataneErrorCode.fromWataneCode(response.getBody().getCode());
            throw new CustomCodeException(
                    errorCode.getMessage(),
                    errorCode.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void validateWataneTransactionResponse(ResponseEntity<WataneBaseResponse<WataneTransactionResult>> response) {
        if (Objects.isNull(response.getBody())) {
            throw new CustomCodeException(
                    WataneErrorCode.GENERAL_EXCEPTION.getMessage(),
                    WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (Objects.isNull(response.getBody())
                || Boolean.FALSE.equals(response.getBody().getSuccess())
                || Objects.isNull(response.getBody().getData())
                || response.getBody().getData().getTransactionStatus() == WATANE_TRANSACTION_FAIL
                || Objects.isNull(response.getBody().getData().getCodes())
                || response.getBody().getData().getCodes().isEmpty()) {
            log.error("Error response from Watane: {}", response.getBody().getCode());
            WataneErrorCode errorCode = WataneErrorCode.fromWataneCode(response.getBody().getCode());
            throw new CustomCodeException(
                    errorCode.getMessage(),
                    errorCode.getAquaCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String getRequestTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

    @Scheduled(cron = "${watane.check-trans.interval}")
    public void scheduleCheckWataneTransaction() {
        log.info("Checking Watane transaction status");
        // get list of 3rd party history where status is IN_PROGRESS and system = watane
        List<ThirdPartyCallAPIHistory> historyList = thirdPartyCallAPIHistoryRepository
                .findAllBySystemAndResultAndAction(SystemType.WATANE.name(), Watane3rdPartyResult.IN_PROGRESS.name(), ThirdPartyAction.PURCHASE.name());

        if (CollectionUtils.isEmpty(historyList)) {
            log.info("No Watane transaction need to check");
            return;
        }
        for (ThirdPartyCallAPIHistory history : historyList) {

            var logEntity = thirdPartyCallAPIHistoryService.makeNewOutBound(null, SystemType.WATANE);
            logEntity.setGoodsId(history.getGoodsId());
            try {
                WataneTransactionRequestBody body = WataneTransactionRequestBody.builder()
                        .requestTime(getRequestTime())
                        .transactionId(history.getTransactionId())
                        .build();
                WataneProduct product = getTransactionStatus(body, logEntity);
                saveExtPin(history, product);

            } catch (Exception e) {
                logEntity.setResponseBody(e.getMessage());
                logEntity.setResponseTime(new Date());
                logEntity.setDescription(e.getMessage());
                log.error("Call api create Watane Voucher error: {}", e.getMessage(), e);
                throw new CustomCodeException(
                        WataneErrorCode.GENERAL_EXCEPTION.getMessage(),
                        WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            } finally {
                log.info("Save log call api");
                thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity);
            }
        }
        // iterate through list and call Watane API to check status
        // if success, save that pin to extpin and mark as available
        // if fail, log fail
        // if processing, skip
        // if timeout, skip
        // if older than 30 days, mark as fail
    }

    private ExtPin saveExtPin(ThirdPartyCallAPIHistory logEntity, WataneProduct product) {
        ExternalPinUpload externalPinUpload = ExternalPinUpload.builder()
                .uploadName(logEntity.getTransactionId())
                .goodsId(logEntity.getGoodsId())
                .rowCount(ONE_ITEM_AT_A_TIME)
                .memo("System Create Watane voucher")
                .status(ExternalPinUploadStatus.UPLOAD_COMPLETED)
                .build();
        log.info("Save ExternalPinUpload: {}", externalPinUpload.toString());
        externalPinUpload = externalPinUploadRepository.save(externalPinUpload);
        Date pinExpireTime = DateUtils.getDateFromWataneFormat(product.getValidTo());

        Long externalPinUploadId = externalPinUpload.getId();
        ExtPin newExtPin = ExtPin.builder()
                .extPinNo(product.getCode())
                .goodsId(logEntity.getGoodsId())
                .uploadId(externalPinUploadId)
                .status(ExtPinStatus.RESERVED)
                .password(null)
                .expireTime(pinExpireTime)
                .regId(SYSTEM_REG_ACCOUNT)
                .regDt(new Date())
                .updtId(SYSTEM_REG_ACCOUNT)
                .updtDt(new Date())
                .transactionId(logEntity.getTransactionId())
                .displayType(PinDisplayType.BARCODE)
                .build();

        log.info("Save ExternalPin: {}", newExtPin);
        return extPinRepository.save(newExtPin);
    }
}
