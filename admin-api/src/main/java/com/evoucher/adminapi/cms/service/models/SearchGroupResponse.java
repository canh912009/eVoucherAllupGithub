package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SearchGroupResponse extends BaseDTO {
    private Integer id;
    private String goodsName;
    private String supplierId;
    private String supplierName;
    private String brandId;
    private String brandName;
    private String goodsStatusCode;
    private String supplierGoodsId;
    private Integer supplierContractId;
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
    private String goodsType;
    private String periodType;
    private Double periodTerm;
    private String periodExpireDate;
    private String system;
}
