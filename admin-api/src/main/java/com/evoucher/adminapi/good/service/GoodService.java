package com.evoucher.adminapi.good.service;

import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.good.service.typed_store_service.StoreQueryService;

public interface GoodService {
    void mapAdditionInfo(GoodsRequest goodsRequest, Goods entity) throws CustomCodeException;
    void setGoodToChildren(Goods goods);
    void updateDeletedChildren(Goods oldGood, GoodsRequest newGoods);
    StoreQueryService getStoreQueryService();
    void syncStoreOfAllBrand() throws CustomCodeException;
}
