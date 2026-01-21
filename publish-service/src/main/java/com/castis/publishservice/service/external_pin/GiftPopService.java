package com.castis.publishservice.service.external_pin;

import com.castis.publishservice.config.KeyConfiguration;
import com.castis.publishservice.dto.GoodsDTO;
import com.castis.publishservice.dto.request.GiftPopCreateVoucherRequest;
import com.castis.publishservice.dto.response.GiftPopCreateVoucherResponse;
import com.castis.publishservice.dto.response.VoucherGiftPopResponse;
import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.entity.ExternalPinUpload;
import com.castis.publishservice.entity.ThirdPartyCallAPIHistory;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.repository.ExtPinRepository;
import com.castis.publishservice.repository.ExternalPinUploadRepository;
import com.castis.publishservice.service.ThirdPartyCallAPIHistoryService;
import com.castis.publishservice.service.common.LockingService;
import com.castis.publishservice.utils.DateUtils;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import com.castis.publishservice.utils.enum_template.PinDisplayType;
import com.castis.publishservice.utils.enum_template.SystemType;
import com.castis.publishservice.utils.status.ExternalPinUploadStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("GIFTPOP")
@RequiredArgsConstructor
public class GiftPopService implements IntegratedPinService {
    //todo: write unit test for separate to several requests
    private static final Integer MAX_QUANTITY_PER_REQUEST = 30;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String GIFT_POP_STATUS_SUCCESS = "0000";
    public static final int GIFT_POP_ERROR_CODE = 9000;
    public static final String AQUA_PARTNER_NAME = "AQUA";

    @Value("${gift-pop.authentication-key}")
    private String giftPopAuthenticationKey;

    @Value("${gift-pop.decrypt-pin-key}")
    private String giftPopDecryptPinKey;

    @Value("${gift-pop.url}")
    private String giftPopUrl;

    private final RestTemplate restTemplate;

    private final KeyConfiguration keyConfiguration;

    private final ExternalPinUploadRepository externalPinUploadRepository;

    private final ExtPinRepository extPinRepository;
    private final LockingService lockingService;

    private final ThirdPartyCallAPIHistoryService thirdPartyCallAPIHistoryService;

    public List<ExtPin> orderGiftPopPin(int pinsQuantity, GoodsDTO goods)
            throws GeneralSecurityException, JsonProcessingException, JOSEException {
        RSAKey rsaKey = keyConfiguration.rsaEVoucherKey();

        List<ExtPin> generatedPins = new ArrayList<>();

        while (pinsQuantity > 0) {
            int pinsQuantityWillGenerateInTime = Math.min(pinsQuantity, MAX_QUANTITY_PER_REQUEST);

            String orderNo = generateUniqueOrderNo();

            GiftPopCreateVoucherRequest request = GiftPopCreateVoucherRequest.builder()
                    .authKey(giftPopAuthenticationKey)
                    .goodsId(goods.getSupplierGoodsId())
                    .sendType("API")
                    .smsYN("N")
                    .quantity(pinsQuantityWillGenerateInTime)
                    .orderNo(orderNo)
                    .build();

            String payload = objectMapper.writeValueAsString(request);
            String signPayload = signPayload(payload, rsaKey);

            log.info("Call api Create Gift Pop voucher with payload: {}", payload);
            var logEntity = thirdPartyCallAPIHistoryService.makeNewOutBound(null, SystemType.GIFTPOP);
            GiftPopCreateVoucherResponse response = callAPICreateVoucher(signPayload, request, logEntity);

            GiftPopCreateVoucherResponse.OrderInfo orderInfo = response.getOrderInfo();
            List<VoucherGiftPopResponse> vouchers = response.getVoucherList();

            ExternalPinUpload externalPinUpload = ExternalPinUpload.builder()
                    // todo: cần nghĩ ra tên chung cho publish có thể cho publishId vào đây
                    .uploadName(orderNo)
                    .goodsId(goods.getId())
                    .rowCount(pinsQuantityWillGenerateInTime)
                    .memo("System Create Gift Pop voucher")
                    .status(ExternalPinUploadStatus.UPLOAD_COMPLETED)
                    .build();
            log.info("Save ExternalPinUpload: {}", externalPinUpload.toString());
            externalPinUpload = externalPinUploadRepository.save(externalPinUpload);

            Date pinExpireTime = DateUtils.generateStringDateToDateEndDay(
                    orderInfo.getExpiryDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));

            Long externalPinUploadId = externalPinUpload.getId();
            List<ExtPin> extPins = vouchers.stream()
                    .map(voucher -> ExtPin.builder()
                            .extPinNo(decryptPin(voucher.getPinNo()))
                            .goodsId(goods.getId())
                            .uploadId(externalPinUploadId)
                            .status(ExtPinStatus.AVAILABLE)
                            .password(voucher.getPassword())
                            .expireTime(pinExpireTime)
                            .regId("System")
                            .regDt(new Date())
                            .updtId("System")
                            .updtDt(new Date())
                            .transactionId(voucher.getTrId())
                            .displayType(PinDisplayType.BARCODE)
                            .build())
                    .collect(Collectors.toList());

            log.info("Save list ExternalPin: {}", extPins);
            extPins = extPinRepository.saveAll(extPins);
            generatedPins.addAll(extPins);

            log.info("Update call API log: {}", logEntity.getId());
            logEntity.setUploadId(externalPinUpload.getId());
            thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity);

            pinsQuantity -= pinsQuantityWillGenerateInTime;
        }

        return generatedPins;
    }

    private String signPayload(String payload, RSAKey rsaKey)
            throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, JOSEException {
        PrivateKey privateKey = rsaKey.toPrivateKey();

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(payload.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes = signature.sign();

        String base64Signature = Base64.getEncoder().encodeToString(signatureBytes);
        log.info("Base64 encoded signature: " + base64Signature);

        return base64Signature;
    }

    private GiftPopCreateVoucherResponse callAPICreateVoucher(
            String signPayload, GiftPopCreateVoucherRequest request, ThirdPartyCallAPIHistory logEntity) {
        String urlCreateGiftPopVoucher = giftPopUrl + "/order/voucherIssueList.m12";

        logEntity.setRequestUrl(urlCreateGiftPopVoucher);
        try {
            logEntity.setRequestBody(objectMapper.writeValueAsString(request));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Signature", signPayload);

            HttpEntity<GiftPopCreateVoucherRequest> entity = new HttpEntity<>(request, headers);

            log.info("Call api create voucher Gift pop");
            ResponseEntity<GiftPopCreateVoucherResponse> response = restTemplate
                    .exchange(urlCreateGiftPopVoucher, HttpMethod.POST, entity, GiftPopCreateVoucherResponse.class);
            String responseString = objectMapper.writeValueAsString(response);
            logEntity.setResponseBody(responseString);
            logEntity.setResponseTime(new Date());
            logEntity.setResult(response.getStatusCode().name());
            log.info("Response GiftPopCreateVoucherRequest: {}", responseString);

            log.info("Validate Gift Pop create voucher response");
            validateGiftPopCreateVoucherResponse(response);

            return response.getBody();
        } catch (CustomCodeException e) {
            logEntity.setDescription(e.getMessage());
            log.error("Gift Pop creation error: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logEntity.setResponseBody(e.getMessage());
            logEntity.setResponseTime(new Date());
            logEntity.setDescription(e.getMessage());
            log.error("Call api create Gift Pop Voucher error: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    "Create Gift Pop Voucher error",
                    GIFT_POP_ERROR_CODE,
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            log.info("Save log call api");
            logEntity = thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity);
        }
    }

    private String decryptPin(String value) {
        try {
            if (giftPopDecryptPinKey.length() != 16) {
                log.error("Key length is not 16 bits");
                return null;
            }

            byte[] raw = giftPopDecryptPinKey.getBytes(StandardCharsets.US_ASCII);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] encrypted = Base64.getDecoder().decode(value);

            byte[] original = cipher.doFinal(encrypted);
            return new String(original);
        } catch (Exception ex) {
            log.error("Decrypt Gift Pop Pin error: {}", ex.getMessage(), ex);
            return null;
        }
    }

    private void validateGiftPopCreateVoucherResponse(
            ResponseEntity<GiftPopCreateVoucherResponse> response) throws CustomCodeException {
        String errorMessage = "Error create Gift Pop Voucher";
        HttpStatus statusCode = response.getStatusCode();

        if (statusCode.isError())
            throw new CustomCodeException(errorMessage, GIFT_POP_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);

        if (ObjectUtils.isEmpty(response))
            throw new CustomCodeException(errorMessage, GIFT_POP_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);

        if (ObjectUtils.isEmpty(response.getBody()))
            throw new CustomCodeException(errorMessage, GIFT_POP_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);

        if (ObjectUtils.isEmpty(response.getBody().getOrderInfo()))
            throw new CustomCodeException(errorMessage, GIFT_POP_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);

        if (!GIFT_POP_STATUS_SUCCESS.equals(response.getBody().getOrderInfo().getResCode())) {
            String errorMessageFromGiftPop = response.getBody().getOrderInfo().getResMessage();
            throw new CustomCodeException(errorMessageFromGiftPop, GIFT_POP_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (ObjectUtils.isEmpty(response.getBody().getVoucherList()))
            throw new CustomCodeException(errorMessage, GIFT_POP_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
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
        log.info("gift pop type doesn't need to reserve pin");
    }

    @Override
    public List<ExtPin> getAvailablePin(int totalSize, GoodsDTO goods, Date bookingDate) {

        List<ExtPin> existPin = lockingService.getPins(goods.getId(), ExtPinStatus.AVAILABLE, totalSize, new Date());
        try {
            int numberPinMissing = totalSize - existPin.size();
            log.info("Order more Pin from Gift Pop for goodsId: {} with number: {}",
                    goods.getId(), numberPinMissing);
            if (numberPinMissing > 0) {
                List<ExtPin> orderedPins;
                log.info("call gift pop to order missing external pin");
                orderedPins = orderGiftPopPin(numberPinMissing, goods);
                existPin.addAll(orderedPins);
            }
            return existPin;
        } catch (CustomCodeException e) {
            log.error("Error" + e.getErrorCode(), e);
            //release pin
            lockingService.removePrcDonePins(goods.getId(), existPin.stream().map(ExtPin::getId).collect(Collectors.toList()));
            throw e;
        } catch (Exception e) {
            log.error("Error get pin for Gift Pop: {}", e.getMessage(), e);
            //release pin
            lockingService.removePrcDonePins(goods.getId(), existPin.stream().map(ExtPin::getId).collect(Collectors.toList()));
            throw new CustomCodeException(
                    "Error get pin for Gift Pop with goodsId: " + goods.getId(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ExtPin> getReservedPin(int totalSize, GoodsDTO goods, Date bookingDate) {
        return this.getAvailablePin(totalSize, goods, bookingDate);
    }


}
