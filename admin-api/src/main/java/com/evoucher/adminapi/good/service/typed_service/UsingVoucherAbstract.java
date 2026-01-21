package com.evoucher.adminapi.good.service.typed_service;

import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;

public interface UsingVoucherAbstract {
    void validateGoodByProductType(GoodsRequest request) throws CustomCodeException;
}
