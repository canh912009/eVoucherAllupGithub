package com.evoucher.externalserviceapi.service.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsInfoResponse {
    private Integer id;
    private String goodsName;
    private String goodsDescription;
    private String goodsType;
    private String supplierGoodsId;
    private SupplierResponse supplier;
    private BrandResponse brandPartnerResponse;
    private String periodType;
    private Double periodTerm;
    private String periodExpireDate;
    private String goodsImgName;
    private String goodsImgPath;
    private String validYn;
    private Double listPrice;
    private Double sellPrice;
    private String settlementMethodCode;
    private String vatIncludeYn;
    private Date startDate;
    private Date endDate;
    private CategoryResponse categories;
}
