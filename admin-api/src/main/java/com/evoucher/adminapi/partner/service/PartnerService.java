package com.evoucher.adminapi.partner.service;

import com.evoucher.adminapi.partner.service.model.request.ExternalPublishConvert;
import com.evoucher.adminapi.partner.service.model.response.*;

import java.util.List;
import java.util.UUID;

public interface PartnerService {

    List<BrandPartnerResponse> findAllBrand();

    BrandPartnerResponse findBrandByBrandId(String brandId);

    List<GoodsPartnerResponse> findAllGoods();

    GoodsPartnerInfoResponse findGoodsByGoodsId(Integer goodsId);

    ExternalPublishResponse createExternalPublish(ExternalPublishConvert externalPublish);

    ExternalPublishResponse checkOrderProgressWithTransactionId(UUID transactionId);

    ExternalPublishOrderPinResponse checkOrderProgressWithOrderId(Integer orderId);

    void cancelExternalPublishByTransactionId(UUID transactionId);

    void cancelOrderPinByOrderId(Integer orderId);
}
