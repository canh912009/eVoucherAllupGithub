package com.evoucher.adminapi.cms.service;

import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SearchStoreResponse;
import com.evoucher.adminapi.cms.service.models.StoreDTO;
import com.evoucher.adminapi.cms.service.models.request.StoreRequest;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.good.service.model.gift_pop.GiftpopStoreResponse;
import com.evoucher.adminapi.good.service.model.ur_box.UrBoxGood;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public interface StoreService {
    StoreDTO findById(String id);
    List<StoreDTO> findByBrandId(String brandId);
    List<String> getStoreCodesByStoreCodeIn(List<String> storeCodes) throws CustomCodeException;
    Map<String, String> getStoreCodeStoreIdMapByBrandId(Collection<String> brandIds) throws CustomCodeException;
    StoreDTO createStore(StoreRequest storeRequest);
    Map<String, AtomicInteger> getCurrentStoreOrdinal(Collection<String> brandIds) throws CustomCodeException;
//    List<StoreDTO> createStores(Collection<StoreRequest> requests);
    void autoCreateStores(Collection<StoreRequest> requests);
    void autoCreateStoreWithIdGen(StoreRequest request);
    StoreDTO updateStore(String id, StoreRequest storeRequest);
    String deleteStoreById(String id);
    Page<SearchStoreResponse> searchStoreDTO(FilterSearchCms filterSearchCms);
    void synchronizeDataToFE(List<String> storeIds, String action);
    public StoreRequest urBoxOfficeToRequest(UrBoxGood.Office office, String brandId, String storeId, EnumValidYn valid);
    public StoreRequest giftPopOfficeToRequest(GiftpopStoreResponse.Store store, String brandId, String storeId, EnumValidYn valid, String latitude, String longitude);
}
