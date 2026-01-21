package com.evoucher.adminapi.good.service.typed_service.system;

import com.evoucher.adminapi.auth.service.CodeService;
import com.evoucher.adminapi.auth.service.models.CodeDTO;
import com.evoucher.adminapi.cms.dao.GoodsRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.BrandService;
import com.evoucher.adminapi.cms.service.StoreService;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.cms.service.models.request.StoreRequest;
import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodService;
import com.evoucher.adminapi.good.service.IntegrationPinService;
import com.evoucher.adminapi.good.service.model.gift_pop.*;
import com.evoucher.adminapi.good.service.typed_store_service.ExcludeStoreQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service("giftpop")
@RequiredArgsConstructor
public class GiftPopService implements IntegrationPinService, GoodService {

    private static final String EVOUCHER_GIFT_POP_GET_LIST_BRAND_ERROR = "evoucher.gift.pop.get.list.brand.error";
    private static final String EVOUCHER_GIFT_POP_GET_LIST_GOODS_ERROR = "evoucher.gift.pop.get.list.goods.error";
    private static final String EVOUCHER_GIFT_POP_GET_LIST_STORE_ERROR = "evoucher.gift.pop.get.list.store.error";

    @Value("${gift-pop.authentication-key}")
    private String giftPopAuthenticationKey;

    @Value("${gift-pop.url}")
    private String giftPopUrl;

    private final RestTemplate restTemplate;
    private final CodeService codeService;
    private final StoreService storeService;
    private final GoodsRepository goodsRepository;
    private final BrandService brandService;
    @Getter
    private final ExcludeStoreQueryService storeQueryService;

    public List<GiftPopBrand> getListGiftPopBrand() {
        String urlGetBrandList = giftPopUrl + "/order/brandList.m12";

        try {
            HttpEntity<GiftPopAuthenticationRequest> entity = setGiftPopAuthenticationRequestHttpEntity();

            log.info("Call api get list Gift pop brand");
            ResponseEntity<GiftPopBrandListResponse> response = restTemplate
                    .exchange(urlGetBrandList, HttpMethod.POST, entity, GiftPopBrandListResponse.class);
            log.info("Response GiftPopBrandList: {}", Constant.gson.toJson(response.getBody()));

            GiftPopBrandListResponse responseBody = response.getBody();

            if (ObjectUtils.isEmpty(responseBody)
                    || !Constant.GIFT_POP.GIFT_POP_STATUS_SUCCESS.equals(responseBody.getResCode())) {
                throw new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_GIFT_POP_GET_LIST_BRAND_ERROR),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (CollectionUtils.isEmpty(responseBody.getBrandList())) {
                log.info("List Gift Pop brand is empty");
                return new ArrayList<>();
            }

            return responseBody.getBrandList();
        } catch (HttpClientErrorException e) {
            log.error("Error get list gift pop brand: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage(EVOUCHER_GIFT_POP_GET_LIST_BRAND_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<GiftpopStoreResponse.Store> getStoreByBrand(String brandCode) throws CustomCodeException {
        String getStoreUrl = giftPopUrl + "/order/storeList.m12";

        try {
            HttpEntity<GiftPopGoodsListRequest> entity = setGiftPopGoodsListRequestHttpEntity(brandCode);


            log.info("call gift pop to get stores {}", Constant.toJsonString(entity));
            ResponseEntity<GiftpopStoreResponse> response = restTemplate
                    .exchange(getStoreUrl, HttpMethod.POST, entity, GiftpopStoreResponse.class);
            log.info("response from gift pop: {}", Constant.gson.toJson(response.getBody()));

            GiftpopStoreResponse responseBody = response.getBody();

            if (ObjectUtils.isEmpty(responseBody)
                    || !Constant.GIFT_POP.GIFT_POP_STATUS_SUCCESS.equals(responseBody.getResCode())) {
                throw new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_GIFT_POP_GET_LIST_STORE_ERROR, responseBody.getResCode()),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (CollectionUtils.isEmpty(responseBody.getStoreList())) {
                log.info("List Gift Pop brand is empty");
                return new ArrayList<>();
            }

            return responseBody.getStoreList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error get list gift pop brand: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage(EVOUCHER_GIFT_POP_GET_LIST_BRAND_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<GiftPopGoods> getListGiftPopGoods(String brandCode) {
        String urlGetGoodsList = giftPopUrl + "/order/goodsList.m12";

        try {
            HttpEntity<GiftPopGoodsListRequest> entity = setGiftPopGoodsListRequestHttpEntity(brandCode);

            ResponseEntity<GiftPopGoodsListResponse> response = restTemplate
                    .exchange(urlGetGoodsList, HttpMethod.POST, entity, GiftPopGoodsListResponse.class);

            log.info("Response GiftPopBrandList: {}", Constant.gson.toJson(response.getBody()));
            GiftPopGoodsListResponse responseBody = response.getBody();

            if (ObjectUtils.isEmpty(responseBody)
                    || !Constant.GIFT_POP.GIFT_POP_STATUS_SUCCESS.equals(responseBody.getResCode())) {
                throw new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_GIFT_POP_GET_LIST_GOODS_ERROR),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (CollectionUtils.isEmpty(responseBody.getGoodsList())) {
                log.info("List Gift Pop Goods is empty with brandCode: {}", brandCode);
                return new ArrayList<>();
            }

            return responseBody.getGoodsList();
        } catch (HttpClientErrorException e) {
            log.error("Error get list gift pop goods: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage(EVOUCHER_GIFT_POP_GET_LIST_GOODS_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpEntity<GiftPopGoodsListRequest> setGiftPopGoodsListRequestHttpEntity(String brandCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        GiftPopGoodsListRequest request = GiftPopGoodsListRequest.builder()
                .authKey(giftPopAuthenticationKey)
                .brand(brandCode)
                .build();

        return new HttpEntity<>(request, headers);
    }

    private HttpEntity<GiftPopAuthenticationRequest> setGiftPopAuthenticationRequestHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        GiftPopAuthenticationRequest request = GiftPopAuthenticationRequest.builder()
                .authKey(giftPopAuthenticationKey)
                .build();

        return new HttpEntity<>(request, headers);
    }



    @Override
    public void syncGood(Goods goods, String externalGoodId) {
        // gift pop doesn't have store for each good
        // no need to sync good store
    }

    @Override
    public void validatePartnerGood(GoodsRequest goodsRequest, Object partnerBrandId) {

        boolean isExistProductOfGiftPop = goodsRepository.existsBySupplierGoodsId(goodsRequest.getSupplierGoodsId());
        if (isExistProductOfGiftPop) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.gift.pop.goods.already.exist", goodsRequest.getSupplierGoodsId()),
                    HttpStatus.BAD_REQUEST);
        }

        String productCode = goodsRequest.getSupplierGoodsId();
        String supplierIdRequest = goodsRequest.getSupplierId();
        CodeDTO codeDTO = codeService.findById(
                Constant.GIFT_POP.GIFTPOP_SUPPLIER_ID_IN_CODE_TABLE,
                Constant.GIFT_POP.GIFTPOP_IN_CODE_GROUP_TABLE);
        if (!supplierIdRequest.equals(codeDTO.getCodeName())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.gift.pop.invalid.supplier.id", supplierIdRequest),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<GiftPopGoods> giftPopGoods = this.getListGiftPopGoods((String) partnerBrandId).stream()
                .filter(goods -> productCode.equals(goods.getGoodsId()))
                .findFirst();
        if (giftPopGoods.isEmpty()) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.gift.pop.get.goods.not.exist", productCode),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validatePartnerBrand(String supplierId, String brandCode) throws CustomCodeException {

        CodeDTO codeDTO = codeService.findById(
                Constant.GIFT_POP.GIFTPOP_SUPPLIER_ID_IN_CODE_TABLE,
                Constant.GIFT_POP.GIFTPOP_IN_CODE_GROUP_TABLE);
        if (!supplierId.equals(codeDTO.getCodeName())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.gift.pop.invalid.supplier.id", supplierId),
                    HttpStatus.BAD_REQUEST);
        }


        List<GiftPopBrand> giftPopBrandList = this.getListGiftPopBrand();
        Optional<GiftPopBrand> giftPopBrand = giftPopBrandList.stream()
                .filter(brand -> brandCode.equals(brand.getBrandCode()))
                .findFirst();

        if (giftPopBrand.isEmpty()) {
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.gift.pop.brand.not.exist", brandCode),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void mapAdditionInfo(GoodsRequest goodsRequest, Goods entity) {
        // no need
    }

    @Override
    public void setGoodToChildren(Goods goods) {
        // no need
    }

    @Override
    public void updateDeletedChildren(Goods oldGood, GoodsRequest newGoods) {
        // no need
    }

    @Override
    public void syncStoreOfAllBrand() throws CustomCodeException {
        log.info("trigger sync store for gift pop brand");
        try {
            List<BrandDTO> activeBrands = brandService.findAllBySystemAndValid(SystemType.GIFTPOP, EnumValidYn.Y);

            if (CollectionUtils.isEmpty(activeBrands)) {
                log.info("have no active brand");
                return;
            }

            activeBrands.removeIf(o -> StringUtils.isEmpty(o.getBrandCode()));

            List<GiftpopStoreResponse.Store> giftpopStores = new ArrayList<>();
            HashMap<String, String> storeCodeBrandIdMap = new HashMap<>();

            // get all store from gift pop and put store, brand to storeCodeBrandIdMap
            getPartnerStoreInfo(activeBrands, giftpopStores, storeCodeBrandIdMap);

            List<String> existedStoreId = giftpopStores.stream().map(GiftpopStoreResponse.Store::getStoreCode)
                    .collect(Collectors.toList());
            List<String> inDbCodes = storeService.getStoreCodesByStoreCodeIn(existedStoreId);

            log.info("{} already saved in db", inDbCodes);


            giftpopStores.removeIf(o -> inDbCodes.contains(o.getStoreCode()));

            log.info("sync store: {}", giftpopStores.stream().map(GiftpopStoreResponse.Store::getBrandOfStore).collect(Collectors.toList()));
            log.info("brand code id map: {}", storeCodeBrandIdMap);

            storeCodeBrandIdMap.entrySet().removeIf(code -> inDbCodes.contains(code.getKey()));

            Map<String, AtomicInteger>  brandIdStoreOrdinalMap =
                    storeService.getCurrentStoreOrdinal(new HashSet<>(storeCodeBrandIdMap.values()));
            log.info("brand id store ordinal map: {}", brandIdStoreOrdinalMap);

            Set<StoreRequest> storeRequests = new HashSet<>();
            giftpopStores
                    .forEach(o -> {
                        String brandId = storeCodeBrandIdMap.get(o.getStoreCode());
                        log.info("mapping store {} of brand {}", o.getStoreCode(), brandId);

                        log.info("store ordinal: {}", brandIdStoreOrdinalMap.getOrDefault(brandId, new AtomicInteger()).get() + 1);

                        String storeId = String.format("%s%s%04d", brandId,
                                CmsConstant.UNDERSCORE_SYMBOL,
                                brandIdStoreOrdinalMap.get(brandId).incrementAndGet());

                        log.info("new store id: {}", storeId);
                        Pair<String, String> positions = getPosition(o.getMapCoord());

                        StoreRequest store = storeService
                                .giftPopOfficeToRequest(
                                        o,
                                        brandId,
                                        storeId,
                                        EnumValidYn.Y,
                                        positions.getLeft(),
                                        positions.getRight());

                        log.info("parsed : {} to {}", o, store);

                        storeRequests.add(store);
                    });
            log.info("save new stores");
            if (CollectionUtils.isEmpty(storeRequests)) {
                log.info("no more new store");
            }
            storeService.autoCreateStores(storeRequests);
        } catch (Exception e) {
            log.error("exception while synchronize giftpop store");
            log.error(e.getMessage(), e);
        }
    }

    private void getPartnerStoreInfo(List<BrandDTO> activeBrands,
                                 List<GiftpopStoreResponse.Store> giftpopStores,
                                 HashMap<String, String> storeCodeBrandIdMap) {
        log.info("get all gift pop store for all brand");
        activeBrands.forEach(o -> {
            try {
                List<GiftpopStoreResponse.Store> storeList = this.getStoreByBrand(o.getBrandCode());
                storeList.forEach(s -> storeCodeBrandIdMap.put(s.getStoreCode(), o.getId()));
                giftpopStores.addAll(storeList);
            } catch (Exception e) {
                log.error("exception when get giftpop store of brand {} -> ignored", o.getBrandCode());
                log.error(e.getMessage(), e);
            }
        });
    }

    private Pair<String, String> getPosition(String mapPosition) {
        String latitude = "";
        String longitude = "";
        if (StringUtils.isNotBlank(mapPosition) && mapPosition.split(",").length == 2) {
            String[] positions = mapPosition.split(",");
            longitude = positions[0];
            latitude = positions[1];
        }

        return Pair.of(latitude, longitude);
    }

    @Override
    public void syncBrand(Brand brand, String externalBrandId) throws CustomCodeException {
        try {
            //get all good by brand id
            List<GiftpopStoreResponse.Store> stores = this.getStoreByBrand(externalBrandId);

            // create store
            Set<StoreRequest> storeRequests = new HashSet<>();
            Set<String> existStore = new HashSet<>();
            AtomicInteger increaseId = new AtomicInteger(1);
            stores.forEach(o -> {
                if (o != null && !existStore.contains(o.getStoreCode())) {
                    Pair<String, String> positions = getPosition(o.getMapCoord());
                    storeRequests.add(
                            storeService
                                    .giftPopOfficeToRequest(
                                            o,
                                            brand.getId(),
                                            String.format("%s%s%04d", brand.getId(),
                                                    CmsConstant.UNDERSCORE_SYMBOL,
                                                    increaseId.getAndIncrement()),
                                            EnumValidYn.Y,
                                            positions.getLeft(),
                                            positions.getRight())
                    );
                    existStore.add(o.getStoreCode());
                }

            });

            log.info("create new store: {}", Constant.toJsonString(storeRequests));

            storeService.autoCreateStores(storeRequests);

        } catch (EntityNotFoundException e) {
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage(EVOUCHER_GIFT_POP_GET_LIST_STORE_ERROR, externalBrandId),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
