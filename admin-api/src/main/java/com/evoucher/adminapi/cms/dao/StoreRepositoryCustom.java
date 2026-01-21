package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SearchStoreResponse;
import com.evoucher.adminapi.cms.service.models.StoreSynchronizeDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryCustom {

    List<SearchStoreResponse> searchStore(FilterSearchCms filterSearchCms, Pageable pageable);

    long countStore(FilterSearchCms filterSearchCms);

    List<StoreSynchronizeDTO> findListStoreDTOByListStoreId(List<String> storeIds);
}
