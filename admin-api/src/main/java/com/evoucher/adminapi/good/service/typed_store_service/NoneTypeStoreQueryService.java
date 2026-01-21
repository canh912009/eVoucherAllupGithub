package com.evoucher.adminapi.good.service.typed_store_service;

import com.evoucher.adminapi.admin.enums.StoreQueryType;
import com.evoucher.adminapi.cms.dao.models.Goods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoneTypeStoreQueryService implements StoreQueryService {
    @Override
    public StoreQueryType getStoreQueryType() {
        return StoreQueryType.NONE;
    }

    @Override
    public void setStoreTypeAndRequiredFields(Goods goods, Goods oldGoods) {
        goods.setStoreQueryType(getStoreQueryType());
    }
}
