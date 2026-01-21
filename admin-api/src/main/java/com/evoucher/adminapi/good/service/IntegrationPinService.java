package com.evoucher.adminapi.good.service;

import com.evoucher.adminapi.cms.dao.GoodsRepository;
import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.cms.service.models.request.GoodsRequest;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.common.exception.CustomCodeException;

import java.util.*;

public interface IntegrationPinService {
    EnumSet<SystemType> syncTypes = EnumSet.of(SystemType.UR_BOX, SystemType.GIFTPOP);

    /**
     * validate brand and create all brand store
     * @param brand
     * @param externalBrandId
     */
    void syncBrand(Brand brand, String externalBrandId) throws CustomCodeException;

    /**
     * validate good and get except store
     * @param goods internal good
     * @param externalGoodId external good id
     */
    void syncGood(Goods goods, String externalGoodId);
    void validatePartnerGood(GoodsRequest goodsRequest, Object partnerBrandId) throws CustomCodeException;
    void validatePartnerBrand(String supplierId, String brandCode) throws CustomCodeException;

}
