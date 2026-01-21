package com.evoucher.adminapi.cms.service;


import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.cms.service.models.SearchGroupResponse;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.Date;

public interface GoodsService {

    GoodsDTO findById(Integer id);

    GoodsDTO createGoods(GoodsRequest goodsRequest);

    GoodsDTO updateGoods(Integer id, GoodsRequest goodsRequest);

    Integer delete(Integer id);

    Page<? extends SearchGroupResponse> searchGoods(FilterSearchCms filterSearchCms);

    Page<SearchGroupResponse> searchGoodsIgnorePermissions(FilterSearchCms filterSearchCms);
    void validateExpiredDate(Goods goodsDTO, Date bookingDate) throws CustomCodeException;
    void syncBrandStore();
}
