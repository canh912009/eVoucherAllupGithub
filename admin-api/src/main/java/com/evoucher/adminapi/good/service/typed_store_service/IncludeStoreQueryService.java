package com.evoucher.adminapi.good.service.typed_store_service;

import com.evoucher.adminapi.admin.enums.StoreQueryType;
import com.evoucher.adminapi.cms.dao.models.Goods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IncludeStoreQueryService implements StoreQueryService{
    @Override
    public StoreQueryType getStoreQueryType() {
        return StoreQueryType.INCLUDE;
    }

    @Override
    public void setStoreTypeAndRequiredFields(Goods goods, Goods oldGoods) {
        goods.setStoreQueryType(getStoreQueryType());

        if (oldGoods != null) {
            String includedStore = Optional.ofNullable(oldGoods.getIncludeStoreIds()).orElse("");
            goods.setIncludeStoreIds(includedStore);
        }
    }
}
