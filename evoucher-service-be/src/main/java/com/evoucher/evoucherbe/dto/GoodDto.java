package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.*;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

@Data
public class GoodDto {
    private Long id;
    private String goodsName;
    private String supplierId;
    private String brandId;
    private String goodsStatusCode;
    private String supplierGoodsId;
    private Integer supplierContractId;
    private Double listPrice;
    private Double sellPrice;
    private Double supplyDiscountRate;
    private Double supplyDiscountAmount;
    private Double supplyCommissionRate;
    private EnumValidYn vatIncludeYn;
    private SettlementMethodCode settlementMethodCode;
    private String goodsDescription;
    private String useInfo;
    private String goodsImgPath;
    private String goodsImgName;
    private Date startDate;
    private Date endDate;
    private EnumValidYn validYn;
    private String exceptStoreIds;
    private VoucherTypeCode goodsType;
    private PeriodType periodType;
    private Integer periodTerm;
    private String periodExpireDate;
    private SystemType system;
    Integer usageCount;
}
