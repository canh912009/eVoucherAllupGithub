package com.evoucher.adminapi.good.service.typed_service.system.watane;

import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.client.WataneClient;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodBasicService;
import com.evoucher.adminapi.good.service.GoodService;
import com.evoucher.adminapi.good.service.IntegrationPinService;
import com.evoucher.adminapi.good.service.model.watane.request.*;
import com.evoucher.adminapi.good.service.model.watane.response.WataneBaseResponse;
import com.evoucher.adminapi.good.service.model.watane.response.WataneListResponse;
import com.evoucher.adminapi.good.service.model.watane.response.WataneProduct;
import com.evoucher.adminapi.good.service.typed_store_service.NoneTypeStoreQueryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service("watane")
@RequiredArgsConstructor
public class WataneService implements IntegrationPinService, GoodService {

    @Value("${watane.appId}")
    private String appId;
    @Value("${watane.api-key}")
    private String apiKey;
    @Value("${watane.secret-key}")
    private String secretKey;
    private final WataneClient wataneClient;
    private final WataneSignatureService wataneSignatureService;
    private final ObjectMapper objectMapper;
    private final GoodBasicService basicService;
    @Getter
    private final NoneTypeStoreQueryService storeQueryService;


    public static final String GOOD_ALREADY_EXIST = "watane.goods.already.exist";

    public List<WataneProduct> getGoodList(int start, int length) throws JsonProcessingException {
        try {
            WatanePageRequestBody body = new WatanePageRequestBody();
            // Watane they set page from 0, aQua starts from 1
            int actualStart = start - 1;
            body.setPagination(Pagination.builder().start(actualStart).length(length).build());
            body.setRequestTime(getRequestTime());

            WataneBaseResponse<WataneListResponse<WataneProduct>> responseBody = getWataneProducts(body);
            validateWataneResponse(responseBody);

            return new ArrayList<>(responseBody.getData().getProducts());
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while getting good list: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                    WataneErrorCode.GENERAL_EXCEPTION.getWataneCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public WataneProduct getGoodById(String goodId) {
        try {
            WataneProductDetailRequestBody requestBody = WataneProductDetailRequestBody.builder()
                    .requestTime(getRequestTime())
                    .code(goodId)
                    .build();
            WataneBaseResponse<WataneProduct> wataneProductDetail = getWataneProductDetail(requestBody);
            validateWataneResponse(wataneProductDetail);
            return wataneProductDetail.getData();
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error occurred while getting good list: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                    WataneErrorCode.GENERAL_EXCEPTION.getWataneCode(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private WataneRequestHeader makeHeader(WataneRequestBody body) throws JsonProcessingException {
        String signature = objectMapper.writeValueAsString(body);
        String sign = wataneSignatureService.sign(signature);
        return WataneRequestHeader.builder()
                .appId(appId)
                .signature(sign)
                .apiKey(apiKey)
                .token(generateToken())
                .build();
    }

    private WataneBaseResponse<WataneListResponse<WataneProduct>> getWataneProducts(WatanePageRequestBody body) throws CustomCodeException, JsonProcessingException {
        WataneRequestHeader header = makeHeader(body);
        log.info("Watane get products body: {}", objectMapper.writeValueAsString(body));
        WataneBaseResponse<WataneListResponse<WataneProduct>> responseBody = wataneClient.getGoods(body, header.getAppId(), header.getSignature(), header.getApiKey(), header.getToken());
        log.info("Watane get products response: {}", objectMapper.writeValueAsString(responseBody));
        return responseBody;
    }

    private WataneBaseResponse<WataneProduct> getWataneProductDetail(WataneProductDetailRequestBody body) throws
            CustomCodeException, JsonProcessingException {
        WataneRequestHeader header = makeHeader(body);
        log.info("Watane get detail body: {}", objectMapper.writeValueAsString(body));
        WataneBaseResponse<WataneProduct> responseBody = wataneClient.getGoodsDetail(body, header.getAppId(), header.getSignature(), header.getApiKey(), header.getToken());
        log.info("Watane get detail response: {}", responseBody);
        return responseBody;
    }

    private String generateToken() {
        String input = apiKey + secretKey;
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new CustomCodeException(
                    WataneErrorCode.GENERAL_EXCEPTION.getAquaCode(),
                    WataneErrorCode.GENERAL_EXCEPTION.getMessage(),
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

    public String getRequestTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return now.format(formatter);
    }

    private void validateWataneResponse(WataneBaseResponse<?> response) {
        if (Objects.isNull(response.getSuccess()) || Boolean.FALSE.equals(response.getSuccess())) {
            log.error("Error response from Watane: {}", response.getCode());
            WataneErrorCode errorCode = WataneErrorCode.fromWataneCode(response.getCode());
            throw new CustomCodeException(
                    errorCode.getAquaCode(),
                    errorCode.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void syncBrand(Brand brand, String externalBrandId) throws CustomCodeException {
        // Later if needed
    }

    @Override
    public void syncGood(Goods goods, String externalGoodId) {
        // Later if needed
    }

    @Override
    public void validatePartnerGood(GoodsRequest goodsRequest, Object partnerBrandId) throws CustomCodeException {
        boolean isExistProductOfWatane = basicService.existsBySupplierGoodsId(goodsRequest.getSupplierGoodsId());
        if (isExistProductOfWatane) {
            throw new CustomCodeException(
                    MessageUtils.getMessage(GOOD_ALREADY_EXIST, goodsRequest.getSupplierGoodsId()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validatePartnerBrand(String supplierId, String brandCode) throws CustomCodeException {
        // Later
    }

    @Override
    public void mapAdditionInfo(GoodsRequest goodsRequest, Goods entity) throws CustomCodeException {
        //ignore for watane type
    }

    @Override
    public void setGoodToChildren(Goods goods) {
        //ignore for watane type

    }

    @Override
    public void updateDeletedChildren(Goods oldGood, GoodsRequest newGoods) {
        //ignore for watane type

    }

    @Override
    public void syncStoreOfAllBrand() throws CustomCodeException {
        //ignore for watane type

    }
}
