package com.evoucher.adminapi.good.service.typed_service.system;

import com.evoucher.adminapi.auth.service.CodeService;
import com.evoucher.adminapi.auth.service.models.CodeDTO;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.BrandService;
import com.evoucher.adminapi.cms.service.StoreService;
import com.evoucher.adminapi.cms.service.StoreServiceImpl;
import com.evoucher.adminapi.cms.service.models.BrandDTO;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.cms.service.models.StoreDTO;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.cms.service.models.request.StoreRequest;
import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.common.client.UrBoxClient;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.exception.EntityNotFoundException;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodBasicService;
import com.evoucher.adminapi.good.service.GoodService;
import com.evoucher.adminapi.good.service.IntegrationPinService;
import com.evoucher.adminapi.good.service.model.ur_box.*;
import com.evoucher.adminapi.good.service.typed_store_service.IncludeStoreQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service("ur_box")
@RequiredArgsConstructor
public class UrBoxService implements IntegrationPinService, GoodService {

    public static final String UR_BOX_GET_LIST_BRAND_ERROR = "error.ur-box.get.brand";
    public static final String UR_BOX_GET_LIST_GOOD_ERROR = "error.ur-box.get.good";
    public static final String UR_BOX_GET_GOOD_ERROR = "error.ur-box.get.good.by.id";
    public static final String ERROR_GOOD_NOT_EXIST="error.ur-box.good.not.exist";
    public static final String ERROR_BRAND_NOT_EXIST="error.ur-box.brand.not.exist";
    public static final String ERROR_BRAND_ALREADY_EXIST="error.ur-box.brand.already.exist";
    public static final String ERROR_GOOD_ALREADY_EXIST="error.ur-box.good.already.exist";
    public static final String ERROR_INVALID_SUPPLIER_ID="error.ur-box.invalid.supplier.id";
    public static final String GOOD_ALREADY_EXIST="ur_box.goods.already.exist";

    public static final String SUPPLIER_ID_CODE_GROUP_ID = "UR_BOX";
    public static final String SUPPLIER_ID_CODE_ID = "SUPPLIER_ID";
    public static final String SUCCESS = "1";
    public static final String FAIL = "0";


    @Value("${ur-box.app-secret}")
    private String appSecret;

    @Value("${ur-box.app-id}")
    private Integer appId;
    @Getter
    private final IncludeStoreQueryService storeQueryService;

    private final CodeService codeService;
    private final UrBoxClient urBoxClient;
    private final StoreService storeService;
    private final GoodBasicService basicService;
    private final BrandService brandService;

    public List<UrBoxBrand> getBrandList() {

        try {
            List<UrBoxBrand> result = new ArrayList<>();
            // Create UriComponentsBuilder to build the URL with parameters
            UrBoxBrandListReq request = new UrBoxBrandListReq();
            setUrBoxBaseParam(request);

            UrBoxListResponse<UrBoxBrand> responseBody = getBrandFromUrBox(request);


            if (CollectionUtils.isEmpty(responseBody.getData().getItems())) {
                log.warn("Receive empty list when get Ur Box brands");
                return result;
            }


            result.addAll(responseBody.getData().getItems());

            if (responseBody.getData().getTotalPage() != null && responseBody.getData().getTotalPage() > 1) {
                AtomicInteger page = new AtomicInteger(2);
                while (page.get() < responseBody.getData().getTotalPage()) {
                    request.setPage_no(page.getAndIncrement());
                    UrBoxListResponse<UrBoxBrand> response = getBrandFromUrBox(request);

                    if (response.getData().getItems() != null) {
                        result.addAll(response.getData().getItems());
                    }
                }
            }

            return result;
        } catch (HttpClientErrorException e) {
            log.error("Error get list gift pop brand: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage(UR_BOX_GET_LIST_BRAND_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @NotNull
    private UrBoxListResponse<UrBoxBrand> getBrandFromUrBox(UrBoxBrandListReq request) throws CustomCodeException {
        log.info("get UrBox Brand with request: {}", request);
        UrBoxListResponse<UrBoxBrand> responseBody = urBoxClient.getBrands(request);

        log.info("UrBox response: {}", responseBody);
        if (ObjectUtils.isEmpty(responseBody)
                || !SUCCESS.equals(responseBody.getDone())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage(UR_BOX_GET_LIST_BRAND_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseBody;
    }


    public List<UrBoxGood> getGoodList(String brandCode) {
        log.info("get all good of brand: {}", brandCode);

        List<UrBoxGood> result = new ArrayList<>();
        try {
            if (brandCode == null) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("invalid brand code"),
                        HttpStatus.BAD_REQUEST);
            }

            UrBoxGoodListRequest request = new UrBoxGoodListRequest();
            request.setBrand_id(Integer.valueOf(brandCode))
                    ;
            setUrBoxBaseParam(request);

            UrBoxListResponse<UrBoxGood> responseBody = getGoodFromUrBox(request);



            if (CollectionUtils.isEmpty(responseBody.getData().getItems())) {
                return result;
            }

            result.addAll(responseBody.getData().getItems());

            if (responseBody.getData().getTotalPage() != null && responseBody.getData().getTotalPage() > 1) {
                AtomicInteger page = new AtomicInteger(2);
                while (page.get() < responseBody.getData().getTotalPage()) {
                    request.setPage_no(page.getAndIncrement());
                    UrBoxListResponse<UrBoxGood> response = getGoodFromUrBox(request);

                    if (response.getData().getItems() != null) {
                        result.addAll(response.getData().getItems());
                    }
                }
            }

            return result;

        } catch (HttpClientErrorException e) {
            log.error("Error get list gift pop goods: {}", e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage(UR_BOX_GET_LIST_GOOD_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public UrBoxGood getGoodById(String goodId) {
        log.info("get ur box good by id: {}", goodId);
        try {
            if (goodId == null) {
                throw new CustomCodeException(
                        MessageUtils.getMessage("invalid brand code"),
                        HttpStatus.BAD_REQUEST);
            }
            UrBoxGoodByIdRequest request = new UrBoxGoodByIdRequest().setId(goodId);
            setUrBoxBaseParam(request);

            UrBoxSingleResponse<UrBoxGood> response = urBoxClient.getGoodById(request);
            log.info("ur box response: {}", Constant.toJsonString(response));

            if (ObjectUtils.isEmpty(response)
                    || !SUCCESS.equals(response.getDone())) {
                throw new EntityNotFoundException(
                        MessageUtils.getMessage(ERROR_GOOD_NOT_EXIST, goodId)
                );
            }

            return response.getData();
        } catch (EntityNotFoundException e) {
          throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage(UR_BOX_GET_GOOD_ERROR, goodId),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @NotNull
    private UrBoxListResponse<UrBoxGood> getGoodFromUrBox(UrBoxGoodListRequest request) throws CustomCodeException {
        log.info("get goods: {}", request);
        UrBoxListResponse<UrBoxGood> responseBody = urBoxClient.getGoods(request);
        log.info("UrBox response: {}", responseBody);

        if (ObjectUtils.isEmpty(responseBody)
                || !SUCCESS.equals(responseBody.getDone())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage(UR_BOX_GET_LIST_GOOD_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseBody;
    }

    private void validateSupplierId(String supplierId) throws CustomCodeException {
        CodeDTO codeDTO = codeService.findById(
                SUPPLIER_ID_CODE_ID,
                SUPPLIER_ID_CODE_GROUP_ID);
        if (!supplierId.equals(codeDTO.getCodeName())) {
            throw new CustomCodeException(
                    MessageUtils.getMessage(ERROR_INVALID_SUPPLIER_ID, supplierId),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private void setUrBoxBaseParam(UrBoxBaseRequest request) {
        request.setApp_id(appId)
                .setApp_secret(appSecret);
    }

    @Override
    public void syncBrand(Brand brand, @NotBlank String externalBrandId) {
        try {
            //get all good by brand id
            List<UrBoxGood> urBoxGoods = this.getGoodList(externalBrandId);

            // create store
            Set<StoreRequest> storeRequests = new HashSet<>();
            Set<String> existStore = new HashSet<>();
            AtomicInteger increaseId = new AtomicInteger(1);
            urBoxGoods.forEach(o -> {
                if (o.getOffice()!= null && !o.getOffice().isEmpty()) {
                    o.getOffice().forEach(off -> {
                        if (!existStore.contains(off.getId())) {
                            storeRequests.add(
                                    storeService
                                            .urBoxOfficeToRequest(
                                                    off,
                                                    brand.getId(),
                                                    String.format("%s%s%04d", brand.getId(),
                                                            CmsConstant.UNDERSCORE_SYMBOL,
                                                            increaseId.getAndIncrement()),
                                                    EnumValidYn.Y)
                            );
                            existStore.add(off.getId());
                        }
                    });
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
                    MessageUtils.getMessage(UR_BOX_GET_GOOD_ERROR, externalBrandId),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }



    @Override
    public void validatePartnerGood(GoodsRequest goodsRequest, Object partnerBrandId) {
        boolean isExistProductOfUrbox = basicService.existsBySupplierGoodsId(goodsRequest.getSupplierGoodsId());
        if (isExistProductOfUrbox) {
            throw new CustomCodeException(
                    MessageUtils.getMessage(GOOD_ALREADY_EXIST, goodsRequest.getSupplierGoodsId()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validatePartnerBrand(String supplierId, String brandCode) throws CustomCodeException {
        validateSupplierId(supplierId);

        List<UrBoxBrand> giftPopBrandList = this.getBrandList();
        Optional<UrBoxBrand> giftPopBrand = giftPopBrandList.stream()
                .filter(brand -> brandCode.equals(brand.getId()))
                .findFirst();

        if (giftPopBrand.isEmpty()) {
            throw new CustomCodeException(
                    MessageUtils.getMessage(ERROR_BRAND_NOT_EXIST, brandCode),
                    HttpStatus.BAD_REQUEST);
        }
    }


    @Override
    public void mapAdditionInfo(GoodsRequest goodsRequest, Goods entity) {
        //ignore for urbox type
    }

    @Override
    public void setGoodToChildren(Goods goods) {
        //no need now
    }

    @Override
    public void updateDeletedChildren(Goods oldGood, GoodsRequest newGoods) {
        // no need
    }

    @Override
    public void syncGood(Goods goods, String externalGoodId) {
        try {
            log.info("validate good and get excepted store ids: {}, {}", goods, externalGoodId);
            UrBoxGood urBoxGood = this.getGoodById(externalGoodId);

            List<StoreDTO> allStore = storeService.findByBrandId(goods.getBrandId());
            Set<String> allStoreCode = allStore.stream().map(StoreDTO::getStoreCode).collect(Collectors.toSet());
            log.info("current sync store code: {}", allStoreCode);
            Optional<List<UrBoxGood.Office>> allOffice = Optional.ofNullable(urBoxGood.getOffice());

            Set<String> acceptStoreCodes = allOffice.orElse(new ArrayList<>()).stream().map(UrBoxGood.Office::getId).collect(Collectors.toSet());
            log.info("good stores code: {}", acceptStoreCodes);
            Set<StoreDTO> excepted = allStore.stream().filter(o -> !acceptStoreCodes.contains(o.getStoreCode())).collect(Collectors.toSet());
            // get excepted store id
            log.info("excepted store: {}", excepted);
            //get new store

            List<UrBoxGood.Office> newOffice = allOffice.orElse(new ArrayList<>()).stream().filter(o -> !allStoreCode.contains(o.getId())).collect(Collectors.toList());
            // add new office
            if (!newOffice.isEmpty()) {
                log.info("new store is not empty, synchronize: {}", newOffice);
                newOffice.forEach(o -> storeService.autoCreateStoreWithIdGen(storeService.urBoxOfficeToRequest(o, goods.getBrandId(), null, EnumValidYn.Y)));
            }

            goods.setExceptStoreIds(Constant.toJsonString(excepted.stream().map(StoreDTO::getId).collect(Collectors.toList())));

        } catch (EntityNotFoundException e) {
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void syncStoreOfAllBrand() throws CustomCodeException {
        try {
            log.info("trigger sync store for urbox brand");
            List<BrandDTO> activeBrands = brandService.findAllBySystemAndValid(SystemType.UR_BOX, EnumValidYn.Y);

            if (CollectionUtils.isEmpty(activeBrands)) {
                log.info("there is no ur box brand, skip synchronizing store");
                return;
            }
            // only sync brand had brand code
            activeBrands.removeIf(o -> StringUtils.isEmpty(o.getBrandCode()));

            List<String> brandIds = activeBrands.stream().map(BrandDTO::getId).collect(Collectors.toList());
            log.info("sync store for brands: {}", brandIds);

            HashMap<String, String> existStoreCodeStoreIdMap =
                    new HashMap<>(storeService.getStoreCodeStoreIdMapByBrandId(brandIds));
            log.info("get all urbox good");
            Map<String, GoodsDTO> allActiveGoodMap = basicService.getGoodCodeObjectMapByBrandIdIn(brandIds);
            List<StoreRequest> storeRequests = new ArrayList<>();

            activeBrands.forEach(o ->
                    getAllGood(o.getBrandCode(), o.getId(), existStoreCodeStoreIdMap, allActiveGoodMap, storeRequests));

            if (CollectionUtils.isEmpty(storeRequests)) {
                log.info("have no new store, skip sync");
            }
            log.info("existed store: {}", existStoreCodeStoreIdMap);
            saveNewStoreAndUpdateGoodIncludedStore(storeRequests, existStoreCodeStoreIdMap, brandIds);

            allActiveGoodMap.forEach((key, value) -> setStoreIdsToGood(value, existStoreCodeStoreIdMap));
            log.info("updated store map: {}", existStoreCodeStoreIdMap);

            basicService.updateStoreList(allActiveGoodMap);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private void setStoreIdsToGood(GoodsDTO good, Map<String, String> storeCodeIdMap) {
        List<String> storeIds = new ArrayList<>();
        good.getIncludedStoresIds().forEach(o -> storeIds.add(storeCodeIdMap.get(o)));

        storeIds.removeIf(Objects::isNull);
        good.setIncludeStoreIds(String.join(",", storeIds));
    }

    private void saveNewStoreAndUpdateGoodIncludedStore(List<StoreRequest> newStores,
                                                        HashMap<String, String> existedStoreMap,
                                                        List<String> brandIds) {
        Map<String, AtomicInteger> brandIdStoreOrdinalMap = storeService.getCurrentStoreOrdinal(brandIds);
        newStores.forEach(o -> {
            String storeId = StoreServiceImpl.
                    getStoreIdFromBrandId(o.getBrandId(), brandIdStoreOrdinalMap.get(o.getBrandId()).incrementAndGet());
            log.info("new store: {}", storeId);
            o.setStoreId(storeId);
        });

        storeService.autoCreateStores(newStores);
         newStores.forEach(o -> existedStoreMap.put(o.getStoreCode(), o.getStoreId()));
    }

    private void getAllGood(String brandCode, String brandId, HashMap<String, String> existedStoreMap,
                            Map<String, GoodsDTO> goodCodeGoodMap, List<StoreRequest> newStores) {
        List<UrBoxGood> urBoxGoods = this.getGoodList(brandCode);
                urBoxGoods.forEach(o -> {
            if (goodCodeGoodMap.containsKey(o.getId())) {
                GoodsDTO good = goodCodeGoodMap.get(o.getId());
                log.info("sync store for good {}", good.getId());
                newStores.addAll(forEachGood(o, brandId, good, existedStoreMap ));
            } else {
                log.info("good {} isn't used in e-voucher system", o.getId());
            }
        });
    }

    private List<StoreRequest> forEachGood(UrBoxGood urBoxGood, String brandId, GoodsDTO good, HashMap<String, String> existedStore) {
        List<StoreRequest> storeRequests = new ArrayList<>();
        List<UrBoxGood.Office> stores = Optional.ofNullable(urBoxGood.getOffice()).orElse(new ArrayList<>());

        if (CollectionUtils.isEmpty(stores)) {
            log.info("good {}[{}] has no store", good.getId(), good.getSupplierGoodsId());
        }

        stores.stream().filter(o -> !existedStore.containsKey(o.getId()))
                .forEach(
                        o -> {
                            log.info("create new store: {}", o.getId());
                            StoreRequest newStore = storeService.urBoxOfficeToRequest(o, brandId, null, EnumValidYn.Y);
                            storeRequests.add(newStore);
                        }
                );
        // add all store code to good
        stores.forEach(o -> good.getIncludedStoresIds().add(o.getId()));
        log.info("good {}[{}] stores: {}", good.getId(), good.getSupplierGoodsId(), good.getIncludedStoresIds());
        return storeRequests;
    }


}
