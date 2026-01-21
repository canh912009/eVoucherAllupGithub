package com.evoucher.adminapi.good.service.typed_store_service;

import com.evoucher.adminapi.admin.enums.StoreQueryType;
import com.evoucher.adminapi.cms.dao.models.Goods;

public interface StoreQueryService {
    StoreQueryType getStoreQueryType();
    void setStoreTypeAndRequiredFields(Goods goods, Goods oldGoods);

}
