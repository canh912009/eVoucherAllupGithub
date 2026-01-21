package com.evoucher.adminapi.good.service.typed_service.system;

import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.GoodsServiceImpl;
import com.evoucher.adminapi.cms.service.models.GoodsDTO;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.enums.GoodsType;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.utils.MessageUtils;
import com.evoucher.adminapi.good.service.GoodBasicService;
import com.evoucher.adminapi.good.service.GoodService;
import com.evoucher.adminapi.good.service.ParentGoodService;
import com.evoucher.adminapi.good.service.typed_store_service.NoneTypeStoreQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("choice")
@RequiredArgsConstructor
@Slf4j
public class ChoiceService implements ParentGoodService, GoodService {
    private final GoodBasicService basicService;
    @Getter
    private final NoneTypeStoreQueryService storeQueryService;
    @Override
    public GoodsType getGoodTypeBySystem() {
        return GoodsType.CH;
    }

    @Override
    public void validateChildren(GoodsRequest request) {
        if (CollectionUtils.isEmpty(request.getListGoodsChoice())) {
            throw new CustomCodeException(MessageUtils.getMessage("evoucher.goods.child.goods.list"),
                    HttpStatus.BAD_REQUEST);
        }

        this.validateChildGoods(request.getListGoodsChoice());
    }

    private void validateChildGoods(List<GoodsDTO> listGoodsChoice) {
        Map<Integer, GoodsDTO> goodsMap =
                basicService.getGoodMapByIdIn(listGoodsChoice.stream().map(GoodsDTO::getId)
                        .collect(Collectors.toList()));
        listGoodsChoice.forEach(
                //validate each good must be valid, good system must be use directly
                goods -> {
                    GoodsDTO goodsDb = goodsMap.get(goods.getId());
                    GoodsServiceImpl.validateChildGood(goodsDb, goods.getId());
                });
    }

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
        //later
    }

    @Override
    public void syncStoreOfAllBrand() throws CustomCodeException {
        log.info("trigger sync store for choice brand");
    }
}
