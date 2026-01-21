package com.evoucher.adminapi.good.service.typed_service.system;

import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.good.service.GoodService;
import com.evoucher.adminapi.good.service.typed_store_service.ExcludeStoreQueryService;
import com.evoucher.adminapi.good.service.typed_store_service.IncludeStoreQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("internal")
@Slf4j
@RequiredArgsConstructor
public class InternalService implements GoodService {
    @Getter
    private final ExcludeStoreQueryService storeQueryService;


    @Override
    public void mapAdditionInfo(GoodsRequest goodsRequest, Goods entity) {
        // no need
    }

    @Override
    public void setGoodToChildren(Goods goods) {
        // no need
    }

    @Override
    public void updateDeletedChildren(Goods oldGood, GoodsRequest newGoods) {
        // no need
    }

    @Override
    public void syncStoreOfAllBrand() throws CustomCodeException {
        log.info("trigger sync store for internal brand");
    }
}
