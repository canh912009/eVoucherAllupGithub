package com.castis.publishservice.dto;

import com.castis.publishservice.utils.enum_template.GoodType;
import com.castis.publishservice.utils.enum_template.SystemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDTO {
    private Long id;

    private String goodsName;

    private String supplierId;

    private String brandId;

    private String goodsStatusCd;

    private String supplierGoodsId;

    private Double listPrice;

    private Double sellPrice;

    private Double supplyDiscountRate;

    private Double supplyDiscountAmount;

    private Double supplyCommissionRate;

    private String vatIncludeYn;

    private String settlementMethodCode;

    private String goodsDescription;

    private String useInfo;

    private String goodsImgPath;

    private String goodsImgName;

    private Date startDate;

    private Date endDate;

    private String validYn;

    private String exceptStoreIds;
    private String regId;

    private Date regDt;

    private String updtId;

    private Date updtDt;
    private String periodType;

    private Double periodTerm;

    private String periodExpireDate;
    private SystemType system;
    private GoodType type;
}
