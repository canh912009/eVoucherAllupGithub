package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.BrandRepository;
import com.evoucher.adminapi.cms.dao.StoreRepository;
import com.evoucher.adminapi.cms.dao.SupplierRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Store;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.cms.mapper.StoreMapper;
import com.evoucher.adminapi.cms.service.models.*;
import com.evoucher.adminapi.cms.service.models.request.StoreRequest;
import com.evoucher.adminapi.cms.service.models.request.DataSynchronizeRequest;
import com.evoucher.adminapi.cms.utils.CmsConstant;
import com.evoucher.adminapi.cms.utils.CmsDataUtil;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumRole;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.model.gift_pop.GiftpopStoreResponse;
import com.evoucher.adminapi.good.service.model.ur_box.UrBoxGood;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final String EVOUCHER_STORE_NOT_FOUND = "evoucher.store.not.found";
    private final StoreRepository storeRepository;
    private final BrandRepository brandRepository;
    private final SupplierRepository supplierRepository;
    private final RestTemplate restTemplate;

    private final StoreMapper storeMapper;
    @Value("${servers.publishServer}")
    private String publishServiceUrl;

    @Override
    public StoreDTO findById(String id) {
        log.info("Find Store with id: {}", id);
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_STORE_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));
        StoreDTO storeDTO = storeMapper.toStoreDTO(store);

        log.info("Find Brand with brandId: {}", store.getBrandId());
        Brand brand = brandRepository.findById(store.getBrandId())
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.brand.not.found"),
                        HttpStatus.BAD_REQUEST));
        Supplier supplier = supplierRepository.findByIdAndValidYn(brand.getSupplierId(), EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));
        storeDTO.setBrand(brand);
        storeDTO.setSupplier(supplier);

        return storeDTO;
    }

    @Override
    public List<StoreDTO> findByBrandId(@NotBlank String brandId) {
        return Optional.of(storeRepository.findAllByBrandId(brandId).stream().map(storeMapper::toStoreDTO).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    @Override
    public List<String> getStoreCodesByStoreCodeIn(List<String> storeCodes) throws CustomCodeException {
        log.info("get store code, id map by store code in: {}", storeCodes);
        try {
            if (CollectionUtils.isEmpty(storeCodes)) {
                return new ArrayList<>();
            }
            List<Store> stores = storeRepository.findAllByStoreCodeInAndValidYn(storeCodes, EnumValidYn.Y);
            return Optional.ofNullable(stores).orElse(new ArrayList<>())
                    .stream().map(Store::getStoreCode).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public Map<String, String> getStoreCodeStoreIdMapByBrandId(Collection<String> brandIds) throws CustomCodeException {
        try {
            List<Store> stores = storeRepository.findAllByBrandIdInAndValidYn(brandIds, EnumValidYn.Y);
            return stores.stream().collect(Collectors.toMap(Store::getStoreCode, Store::getId, (old, newOne) -> old));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public StoreDTO createStore(StoreRequest storeRequest) {
        String brandId = storeRequest.getBrandId();

        log.info("Find Brand with brandId: {}", brandId);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.brand.not.found"),
                        HttpStatus.BAD_REQUEST));

        String supplierId = brand.getSupplierId();
        // check supplier already exists and approved
        log.info("Find Supplier by supplierId: {}", brand.getSupplierId());
        Supplier supplier = getSupplierApproved(supplierId);

        // validate user permission for create store
        validatePermissionCreateStore(brandId, supplierId);

        log.info("Create Store ID with supplierId: {} and brandId: {}", supplierId, brandId);
        String storeId = createStoreId(supplierId, brandId);
        if (Objects.isNull(storeId)) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.store.create.id.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // convert to supplier entity
        Store store = storeMapper.toStore(storeRequest);

        // save to database
        store.setId(storeId);
        log.info("Save Store to Database with Id: {}", storeId);
        store = storeRepository.save(store);

        log.info("Update data to FE with storeId: {}", storeId);

        StoreDTO storeDTO = storeMapper.toStoreDTO(store);
        storeDTO.setBrand(brand);
        storeDTO.setSupplier(supplier);

        return storeDTO;
    }

    @Override
    public Map<String, AtomicInteger> getCurrentStoreOrdinal(Collection<String> brandIds) throws CustomCodeException {
        try {
            List<Map<String, Object>> stores = storeRepository.getMaxStoreIdByBrandIdIn(brandIds);
            HashMap<String, AtomicInteger> result = new HashMap<>();

            stores.forEach(o -> {
                int ordinal = 0;
                String storeId = (String) o.get("store_id");
                String brandId = (String) o.get("brand_id");

                if (storeId != null) {
                    ordinal = Integer.parseInt(storeId.replace(brandId.concat(CmsConstant.UNDERSCORE_SYMBOL), ""));
                }
                result.put(brandId, new AtomicInteger(ordinal));
            });

            brandIds.stream().filter(o -> !result.containsKey(o))
                    .forEach(o -> result.put(o, new AtomicInteger(0)));
            return result;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void autoCreateStores(Collection<StoreRequest> requests) {
        try {
            log.info("auto create store: {}", Constant.toJsonString(requests));
            List<Store> stores = new ArrayList<>();
            requests.forEach(o -> stores.add(storeMapper.toStore(o)));
            storeRepository.saveAll(stores);
            log.info("auto crate store successfully");
        } catch (Exception e) {
            throw new CustomCodeException(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void autoCreateStoreWithIdGen(StoreRequest request) {
        log.info("auto create store: {}", Constant.toJsonString(request));
        try {
            Store store = storeMapper.toStore(request);
            String storeId = createStoreId(null, request.getBrandId());
            if (Objects.isNull(storeId)) {
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.store.create.id.error"),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            store.setId(storeId);
            storeRepository.save(store);
            log.info("add store successfully");
        } catch (CustomCodeException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional
    public StoreDTO updateStore(String id, StoreRequest storeRequest) {
        // check supplier exists
        log.info("Find Store with id: {}", id);
        Store storeOld = storeRepository.findById(id)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_STORE_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));
        if (!storeRequest.getBrandId().equals(storeOld.getBrandId()))
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.store.incorrect.brand.id"),
                    HttpStatus.BAD_REQUEST);

        log.info("Find Brand with brandId: {}", storeOld.getBrandId());
        Brand brand = brandRepository.findById(storeOld.getBrandId())
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.brand.not.found"),
                        HttpStatus.BAD_REQUEST));

        // check supplier already exists and approved
        log.info("Find Supplier by supplierId: {}", brand.getSupplierId());
        Supplier supplier = getSupplierApproved(brand.getSupplierId());

        // validate user permission for create store
        validatePermissionCreateStore(brand.getId(), brand.getSupplierId());

        // convert to entity
        Store store = storeMapper.toStore(storeRequest);

        // save to database
        store.setId(id);
        store.setBrandId(storeOld.getBrandId());

        log.info("Save Store to Database with id: {}", id);
        store = storeRepository.save(store);

        StoreDTO storeDTO = storeMapper.toStoreDTO(store);
        storeDTO.setBrand(brand);
        storeDTO.setSupplier(supplier);

        return storeDTO;
    }

    @Override
    @Transactional
    public String deleteStoreById(String id) {
        Store store = storeRepository.findByIdAndValidYn(id, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(
                        MessageUtils.getMessage(EVOUCHER_STORE_NOT_FOUND),
                        HttpStatus.BAD_REQUEST));

        store.setValidYn(EnumValidYn.N);
        storeRepository.save(store);
        return id;
    }

    @Override
    public Page<SearchStoreResponse> searchStoreDTO(FilterSearchCms filterSearchCms) {
        int page = ObjectUtils.isEmpty(filterSearchCms.getPage()) ? 0 : filterSearchCms.getPage() - 1; //pageable count from 0
        int pageSize = ObjectUtils.isEmpty(filterSearchCms.getPageSize()) ? 10 : filterSearchCms.getPageSize();
        Pageable pageable = PageRequest.of(page, pageSize);

        log.info("Search Store");
        List<SearchStoreResponse> stores = storeRepository.searchStore(filterSearchCms, pageable);
        long countStore = 0;
        if (!CollectionUtils.isEmpty(stores)) {
            log.info("Count Store");
            countStore = storeRepository.countStore(filterSearchCms);
        }
        return new PageImpl<>(stores, pageable, countStore);
    }

    @Override
    public void synchronizeDataToFE(List<String> storeIds, String action) {
        log.info("Get list Store with ids: {}", storeIds);
        List<StoreSynchronizeDTO> stores = storeRepository.findListStoreDTOByListStoreId(storeIds);

        String url = publishServiceUrl + "/store/synchronize";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var payload = stores.stream().map(s -> {
            return new DataSynchronizeRequest<StoreSynchronizeDTO>(action, s);
        }).collect(Collectors.toList());
        HttpEntity<List<DataSynchronizeRequest<StoreSynchronizeDTO>>> entity = new HttpEntity<>(payload, headers);

        log.info("Call api publish-service synchronize with store: {}", Constant.gson.toJson(payload));
        ResponseEntity<Void> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

        log.info("Synchronize stores response: {}", response);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.store.update.data.to.fe.error"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public StoreRequest urBoxOfficeToRequest(UrBoxGood.Office office, String brandId, String storeId, EnumValidYn valid) {
        return storeMapper.toRequestDTO(office, storeId, brandId, valid);
    }

    @Override
    public StoreRequest giftPopOfficeToRequest(GiftpopStoreResponse.Store store,
                                               String brandId, String storeId, EnumValidYn valid, String latitude, String longitude) {
        return storeMapper.toRequestDTO(store, brandId, storeId, valid, latitude, longitude);
    }

    private Supplier getSupplierApproved(String brand) {
        Supplier supplier = supplierRepository.findByIdAndValidYn(brand, EnumValidYn.Y)
                .orElseThrow(() -> new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.found"),
                        HttpStatus.BAD_REQUEST));
        if (!ApproveStatus.APPRV.equals(supplier.getApproveStatusCode()))
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.supplier.not.approved"),
                    HttpStatus.BAD_REQUEST);
        return supplier;
    }

    private String createStoreId(String supplierId, String brandId) {
        String result;
        String regexSearch;
        String idPrefix;

        if (Objects.isNull(brandId)) { // if brandId is null => id = 1234567890-000-1234
            result = supplierId + "-000" + Constant.STORE.STORE_FIRST;
            regexSearch = supplierId + "-000" + Constant.Common.REGEX_SEARCH_SYMBOL;
            idPrefix = supplierId + "-000-";
        } else { // if brandId not null => id = 1234567890_123_1234
            result = brandId + Constant.STORE.STORE_FIRST;
            regexSearch = brandId + CmsConstant.UNDERSCORE_SYMBOL + Constant.Common.REGEX_SEARCH_SYMBOL;
            idPrefix = brandId + CmsConstant.UNDERSCORE_SYMBOL;
        }

        log.info("Find list Store ID with supplierId: {} and brandId: {}", supplierId, brandId);
        List<String> storeIds = storeRepository.findAllStoreIdByRegexIdAndIdASC(regexSearch);

        return CmsDataUtil.createId(storeIds, idPrefix, Constant.STORE.STORE_SIZE_MAX, result);
    }
    private void validatePermissionCreateStore(String brandId, String supplierId) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        log.info("User create store: {}", user);

        switch (EnumRole.valueOf(user.getAdminType())) {
            case ROLE_ADMIN:
            case ROLE_OPERATOR:
                break;

            case ROLE_SUPPLIER: {
                if (!supplierId.equals(user.getAdminCorpId())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                            HttpStatus.BAD_REQUEST);
                }
                break;
            }
            case ROLE_BRAND: {
                if (!brandId.equals(user.getAdminCorpId())) {
                    throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                            HttpStatus.BAD_REQUEST);
                }
                break;
            }
            default:
                throw new CustomCodeException(MessageUtils.getMessage("evoucher.account.permission"),
                        HttpStatus.UNAUTHORIZED);
        }
    }

    public static String getStoreIdFromBrandId(String brandId, Integer ordinal) {
        return brandId + CmsConstant.UNDERSCORE_SYMBOL + ordinal;
    }
}
