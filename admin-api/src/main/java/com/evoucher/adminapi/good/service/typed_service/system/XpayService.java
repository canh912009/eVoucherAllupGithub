package com.evoucher.adminapi.good.service.typed_service.system;

import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.dao.models.XpayGood;
import com.evoucher.adminapi.cms.mapper.GoodsMapper;
import com.evoucher.adminapi.cms.service.models.XpayGoodDto;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.good.service.GoodService;
import com.evoucher.adminapi.good.service.IntegrationPinService;
import com.evoucher.adminapi.good.service.typed_store_service.NoneTypeStoreQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.evoucher.adminapi.common.utils.Common.getErrorMsgByCode;


@Service("xpay")
@Slf4j
@RequiredArgsConstructor
public class XpayService implements IntegrationPinService, GoodService {
    private static final GoodsMapper goodsMapper = GoodsMapper.INSTANCE;
    @Getter
    private final NoneTypeStoreQueryService storeQueryService;
    private static class ErrorCode {
        private ErrorCode() {}
        public static final int XPAY_GOOD_LIST_EMPTY = 20203;
    }
    @Override
    public void syncBrand(Brand brand, String externalBrandId) {
        //later
    }

    @Override
    public void syncGood(Goods goods, String externalGoodId) {
//later
    }

    @Override
    public void validatePartnerGood(GoodsRequest goodsRequest, Object partnerBrandId) throws CustomCodeException {
//later
    }

    @Override
    public void validatePartnerBrand(String supplierId, String brandCode) throws CustomCodeException {
//later
    }

    @Override
    public void mapAdditionInfo(GoodsRequest goodsRequest, Goods entity) throws CustomCodeException {
        if (CollectionUtils.isEmpty(goodsRequest.getXpayGoods())) {
            log.error("xpay good is empty");
            throw new CustomCodeException(
                    getErrorMsgByCode(ErrorCode.XPAY_GOOD_LIST_EMPTY),
                    HttpStatus.BAD_REQUEST
            );
        }
        entity.setXpayGoods(goodsMapper.toXpayGoodEntityList(goodsRequest.getXpayGoods()));
    }

    @Override
    public void setGoodToChildren(Goods goods) {
        Optional.ofNullable(goods.getXpayGoods()).orElse(new ArrayList<>())
                .forEach(o -> o.setXpayParentGood(goods));
    }

    @Override
    public void updateDeletedChildren(Goods oldGood, GoodsRequest newGoods) {
        if (CollectionUtils.isEmpty(newGoods.getXpayGoods())) {
            log.error("new good has empty Xpay good list");
            throw new CustomCodeException(getErrorMsgByCode(ErrorCode.XPAY_GOOD_LIST_EMPTY), HttpStatus.BAD_REQUEST);
        }
        Set<Long> newIdList = newGoods.getXpayGoods().stream()
                .map( o -> {
                    o.setValidYn(EnumValidYn.Y);
                    return o.getId();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, XpayGood> oldMap = oldGood.getXpayGoods().stream()
                .collect(Collectors.toMap(XpayGood::getId, entity -> entity));

        Set<Long> removed = new HashSet<>(oldMap.keySet());

        removed.removeAll(newIdList);

        oldMap.forEach((k, v) -> {
            if (removed.contains(k)) {
                XpayGoodDto dto = goodsMapper.toDTO(v);
                dto.setValidYn(EnumValidYn.N);
                newGoods.getXpayGoods().add(dto);
            }
        });

    }

    @Override
    public void syncStoreOfAllBrand() throws CustomCodeException {
        log.info("trigger sync store for Xpay brand");
    }
}
