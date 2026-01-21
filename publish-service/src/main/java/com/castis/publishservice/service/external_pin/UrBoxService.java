package com.castis.publishservice.service.external_pin;

import com.castis.publishservice.client.FeignException;
import com.castis.publishservice.client.UrBoxClient;
import com.castis.publishservice.dto.*;
import com.castis.publishservice.dto.request.ur_box.UrBoxGiftReq;
import com.castis.publishservice.dto.request.ur_box.UrBoxSignData;
import com.castis.publishservice.dto.response.ur_box.UrBoxGift;
import com.castis.publishservice.dto.response.ur_box.UrBoxSingleResponse;
import com.castis.publishservice.entity.ExtPin;
import com.castis.publishservice.entity.ExternalPinUpload;
import com.castis.publishservice.entity.ThirdPartyCallAPIHistory;
import com.castis.publishservice.exception.defineException.CustomCodeException;
import com.castis.publishservice.repository.ExtPinRepository;
import com.castis.publishservice.repository.ExternalPinUploadRepository;
import com.castis.publishservice.service.common.LockingService;
import com.castis.publishservice.service.ThirdPartyCallAPIHistoryService;
import com.castis.publishservice.utils.DateUtils;
import com.castis.publishservice.utils.enum_template.ExtPinStatus;
import com.castis.publishservice.utils.enum_template.PinDisplayType;
import com.castis.publishservice.utils.enum_template.SystemType;
import com.castis.publishservice.utils.status.ExternalPinUploadStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.castis.publishservice.utils.Constants.ERROR_CODE.INTERNAL_ERROR_CODE;

@RequiredArgsConstructor
@Service("UR_BOX")
@Slf4j
public class UrBoxService implements IntegratedPinService{
    public static final String SUCCESS = "1";
    //todo: write unit test for separate to several requests
    private static final Integer MAX_QUANTITY_PER_REQUEST = 30;



    private final ObjectMapper objectMapper;

    private final SignatureService signatureService;
    private final UrBoxClient urBoxClient;
    private final ExternalPinUploadRepository externalPinUploadRepository;
    private final ExtPinRepository extPinRepository;
    private final ThirdPartyCallAPIHistoryService thirdPartyCallAPIHistoryService;
    private final LockingService lockingService;

    @Value("${ur-box.app-secret}")
    private String appSecret;
    @Value("${ur-box.app-id}")
    private Integer appId;
    @Value("${ur-box.url}")
    private String urBoxUrl;
    @Value("${ur-box.path.buy-gift}")
    private String buyGiftPath;
    @Value("${ur-box.campaign_code}")
    private String campaignCode;
    @Value("${ur-box.transaction_info.phone}")
    private String transactionPhone;
    @Value("${ur-box.transaction_info.email}")
    private String transactionEmail;
    @Value("${ur-box.transaction_info.full_name}")
    private String transactionFullName;
    @Value("${ur-box.transaction_info.site_user_id}")
    private String aQuaSiteId;


    public List<ExtPin> orderUrboxPin(int pinsQuantity, GoodsDTO goods)
            throws JsonProcessingException {
        List<ExtPin> extPins = new ArrayList<>();
        if (pinsQuantity > 0) {
            String transactionId = UUID.randomUUID().toString();
            var request = new UrBoxGiftReq();
            request.setSite_user_id(aQuaSiteId)
                    .setTtfullname(transactionFullName)
                    .setTtemail(transactionEmail)
                    .setTtphone(transactionPhone)
                    .setCampaign_code(campaignCode)
                    .setTransaction_id(transactionId)
                    .setApp_secret(appSecret)
                    .setApp_id(String.valueOf(appId));

            UrBoxDataBuy quantity = new UrBoxDataBuy();
            quantity.setQuantity(String.valueOf(pinsQuantity));
            quantity.setPriceId(goods.getSupplierGoodsId());

            request.setDataBuy(List.of(quantity));

            var signData = new UrBoxSignData();
            signData.setTransaction_id(transactionId)
                    .setDataBuy(List.of(quantity))
                    .setCampaign_code(campaignCode)
                    .setApp_id(String.valueOf(appId))
                    .setSite_user_id(aQuaSiteId)
                    .setApp_secret(appSecret);

            String signature = signatureService.sign(signData);
            log.info("request ur box to buy voucher: signature-{}, request-{}", signature, request);

            var logEntity = thirdPartyCallAPIHistoryService.makeNewOutBound(urBoxUrl.concat(buyGiftPath), SystemType.UR_BOX);
            UrBoxGift urBoxGift = makeUrBoxExternalPin(logEntity, signature, request);


            try {
                ExternalPinUpload externalPinUpload = ExternalPinUpload.builder()
                        .uploadName(transactionId)
                        .goodsId(goods.getId())
                        .rowCount(pinsQuantity)
                        .memo("System Create UrBox voucher")
                        .status(ExternalPinUploadStatus.UPLOAD_COMPLETED)
                        .build();
                log.info("Save ExternalPinUpload: {}", externalPinUpload.toString());
                externalPinUpload = externalPinUploadRepository.save(externalPinUpload);

                Long externalPinUploadId = externalPinUpload.getId();

                extPins = convertUrBoxPin(urBoxGift, externalPinUploadId, goods.getId(), transactionId);

                log.info("Save list ExternalPin: {}", extPins);
                extPins = extPinRepository.saveAll(extPins);

                log.info("Update call API log: {}", logEntity.getId());
                logEntity.setUploadId(externalPinUpload.getId());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw e;
            } finally {
                thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity);
            }
        }


        return extPins;
    }

    @SuppressWarnings("unchecked")
    private UrBoxGift makeUrBoxExternalPin(ThirdPartyCallAPIHistory logEntity, String signature, UrBoxGiftReq request) throws CustomCodeException, JsonProcessingException {

        try {
            logEntity.setRequestBody(objectMapper.writeValueAsString(request));
            UrBoxSingleResponse<UrBoxGift> response = urBoxClient.buyGift(signature, request);
            logEntity.setResult(HttpStatus.OK.name());
            logEntity.setResponseBody(objectMapper.writeValueAsString(response));

            if (ObjectUtils.isEmpty(response)
                    || !SUCCESS.equals(response.getDone())) {
                logEntity.setResult("Fail");
                throw new CustomCodeException("UrBox Voucher Creating Error: " + Objects.toString(response.getMsg(), ""), INTERNAL_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            log.info("make a log: {}", logEntity);
            if ( ObjectUtils.isEmpty(response.getData())
                    || ObjectUtils.isEmpty(response.getData().getCart())
                    || ObjectUtils.isEmpty(response.getData().getCart().getCode_link_gift())
                    ) {
                logEntity.setResult("Fail");
                throw new CustomCodeException("UrBox Voucher Creating Error: " + Objects.toString(response.getMsg(), ""), INTERNAL_ERROR_CODE, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.info("urbox response with body: {}", objectMapper.writeValueAsString(response.getData()));

            return response.getData();
        } catch (CustomCodeException e) {
            logEntity.setResult("Fail");
            logEntity.setDescription(Objects.toString(e.getMessage(), ""));
            throw e;
        }
        catch (FeignException e) {
            log.error("call ur box-{} return exception: status: {}, body: {}", e.getUrl(), e.getStatus(), e.getBody());
            logEntity.setResult("Fail");
            logEntity.setDescription("api calling exception" + Objects.toString(e.getOriginalMessage(), ""));
            logEntity.setResponseBody(objectMapper.writeValueAsString(e.getBody()));
            throw new CustomCodeException(
                    "UrBox Gift Creating Exception: " + Objects.toString(e.getOriginalMessage(), ""),
                    INTERNAL_ERROR_CODE,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            log.error("call UrBox gift return exception");
            log.error(e.getMessage(), e);
            logEntity.setResult("Fail");
            logEntity.setDescription("server exception" + Objects.toString(e.getMessage(), ""));
            throw new CustomCodeException(
                    "UrBox Gift Creating Exception: " + Objects.toString(e.getMessage(), ""),
                    INTERNAL_ERROR_CODE,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        finally {
            logEntity.setResponseTime(new Date());
            logEntity.setId(thirdPartyCallAPIHistoryService.saveThirdPartyCallAPIHistory(logEntity).getId());
        }
    }

    private List<ExtPin> convertUrBoxPin(UrBoxGift urBoxPin, Long externalPinUploadId, Long goodId, String transactionId) {
        return urBoxPin.getCart().getCode_link_gift().stream()
                .map(urBox -> ExtPin.builder()
                        .extPinNo(urBox.getCode())
                        .goodsId(goodId)
                        .uploadId(externalPinUploadId)
                        .status(ExtPinStatus.AVAILABLE)
                        .password(urBox.getSerial())
                        .expireTime(DateUtils.getDateFromSlashFormat(urBox.getExpired()))
                        .regId("System")
                        .regDt(new Date())
                        .updtId("System")
                        .updtDt(new Date())
                        .transactionId(transactionId)
                        .displayType(getDisplayTypeByUrBoxType(urBox.getCode_display_type()))
                        .build())
                .collect(Collectors.toList());
    }

    private PinDisplayType getDisplayTypeByUrBoxType(int urBoxCode) throws CustomCodeException {
        switch (urBoxCode) {
            case 1:
                return PinDisplayType.QRCODE;
            case 2:
                return PinDisplayType.BARCODE;
            case 4:
                return PinDisplayType.TEXT;
            case 5:
                return PinDisplayType.QRBAR;
            default:
                throw new CustomCodeException(
                        "voucher display type is not supported",
                        HttpStatus.BAD_REQUEST
                );
        }
    }

    @Override
    public void reservePin(Integer userSize, GoodsDTO goods, Date bookingDate) {
        log.info("urbox type no need to reserve pin");
    }

    @Override
    public List<ExtPin> getAvailablePin(int totalSize, GoodsDTO goods, Date bookingDate) {
        List<ExtPin> existPins =
                lockingService.getPins(goods.getId(),ExtPinStatus.AVAILABLE, totalSize, new Date());
        try {

            int numberPinMissing = totalSize - existPins.size();
            log.info("Order more Pin from Gift Pop for goodsId: {} with number: {}",
                    goods.getId(), numberPinMissing);
            if (numberPinMissing > 0) {
                List<ExtPin> orderedPins = new ArrayList<>();
                log.info("call ur box to order missing external pins");

                int pinToOrder = numberPinMissing;
                while (pinToOrder > 0) {
                    int orderSize = Math.min(pinToOrder, MAX_QUANTITY_PER_REQUEST);
                    log.info("call ur box to order {} missing external pins", orderSize);
                    List<ExtPin> orderedPinsThisTime = orderUrboxPin(orderSize, goods);
                    orderedPins.addAll(orderedPinsThisTime);
                    pinToOrder -= orderSize;
                }
                existPins.addAll(orderedPins);
            }
            return existPins;
        } catch (CustomCodeException e) {
            log.error("ERROR: " + e.getErrorCode(), e);
            lockingService.removePrcDonePins(goods.getId(), existPins.stream().map(ExtPin::getId).collect(Collectors.toList()));
            throw e;
        } catch (Exception e) {
            log.error("Error get pin for UrBox: {}", e.getMessage(), e);
            lockingService.removePrcDonePins(goods.getId(), existPins.stream().map(ExtPin::getId).collect(Collectors.toList()));
            throw new CustomCodeException(
                    "Error get pin for UrBox with goodsId: " + goods.getId(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ExtPin> getReservedPin(int totalSize, GoodsDTO goods, Date bookingDate) {
        return this.getAvailablePin(totalSize, goods, bookingDate);
    }
}
