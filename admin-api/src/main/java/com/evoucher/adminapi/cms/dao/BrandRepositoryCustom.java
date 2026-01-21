package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SearchBrandResponse;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.BrandSearchDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BrandRepositoryCustom {

    List<SearchBrandResponse> searchBrand(FilterSearchCms filterSearchCms, Pageable pageable);

    long countBrand(FilterSearchCms filterSearchCms);
    List<BrandSearchDTO> searchBrandByBrandIdAndBrandTitle(String supplierId, String brandID, String brandTitle, Pageable pageable);
    long countBrandByBrandIdAndBrandTitle(String supplierId, String brandId, String brandTitle);
    List<Object[]> searchBrandByCategoryAndFilter(FilterSearchCms filter, Pageable pageable);
    Long countBrandByCategoryAndFilter(FilterSearchCms filter, Pageable pageable);
}
