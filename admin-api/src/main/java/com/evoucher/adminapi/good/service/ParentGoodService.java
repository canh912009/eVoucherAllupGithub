package com.evoucher.adminapi.good.service;

import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.enums.GoodsType;
import com.evoucher.adminapi.common.exception.CustomCodeException;

public interface ParentGoodService {
    GoodsType getGoodTypeBySystem();
    void validateChildren(GoodsRequest request) throws CustomCodeException;
}
