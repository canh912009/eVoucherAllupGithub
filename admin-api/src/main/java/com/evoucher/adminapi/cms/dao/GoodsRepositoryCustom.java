package com.evoucher.adminapi.cms.dao;

import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.SearchGroupResponse;
import com.evoucher.adminapi.cms.service.models.vnptEPay.response.GiftSearchWithDateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GoodsRepositoryCustom {
    List<SearchGroupResponse> searchGoods(FilterSearchCms filterSearchCms, Pageable pageable, boolean ignorePermissions);

    Long countGoods(FilterSearchCms filterSearchCms, Pageable pageable, boolean ignorePermissions);
    List<GiftSearchWithDateDTO> searchVnptGift(String supplierId, String brandId, String brandTitle, String giftTitle, Pageable pageable);
    Long countVnptGift(String supplierId, String brandId, String brandTitle, String giftTitle);
    List<Object[]> searchGoodByCatBrandAndFilter(FilterSearchCms filer, Pageable pageable);
    Long countGoodByCatBrandAndFilter(FilterSearchCms filer, Pageable pageable);
}
