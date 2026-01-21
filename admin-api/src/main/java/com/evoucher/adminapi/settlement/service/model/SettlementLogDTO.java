package com.evoucher.adminapi.settlement.service.model;

import com.evoucher.adminapi.common.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementLogDTO {
    private Integer logId;
    private String ev;
    private String settlementLogType;
    private Integer publishId;
    private String publishName;
    private Integer publishDetailId;
    private String pin;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date transactionDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date logCreateDate;
    private String voucherTypeCode;
    private Integer goodsId;
    private String goodsName;
    private String customerId;
    private String customerName;
    private String supplierId;
    private String supplierName;
    private String companyName;
    private String brandId;
    private String brandName;
    private String storeId;
    private String storeName;
    private String managerName;
    private String userMobileNumber;
    private String userEmail;
    private String staffMobileNumber;
    private String settlementCompleteYn;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date settlementCompleteDate;
    private String settlementTarget;
    private String settlementMethodCode;
    private Double listPrice;
    private Double salesPrice;
    private Double discountRate;
    private Double discountAmount;
    private Double discountAppliedAmount;
    private Double settlementAmount;
    private String vatIncludeYn;
    private Double vatAmount;
    private Double commissionRate;
    private Double commissionAmount;
    private Double sendCost;
    private String settlementExceptReasonCode;
    private String settlementExceptReason;
    private Double remainAmount;
    private Double initAmount;
    private Integer campaignId;
    private String campaignName;
    private String serialNo;
    @JsonFormat(pattern= Constant.Common.COMMON_DATETIME_FORMAT)
    private Date activationDate;
    private String originalEv;
    private String parentVoucherEv;
}
