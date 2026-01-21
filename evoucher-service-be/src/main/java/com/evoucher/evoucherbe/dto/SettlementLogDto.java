package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SettlementLogDto {
    Integer logId;
    String ev;
    SettlementLogType settlementLogType;
    Integer publishId;
    Integer publishDetailId;
    Integer transactionId;
    Date transactionDate;
    Date logCreateDate;
    VoucherTypeCode voucherTypeCode;
    Long goodsId;
    String customerId;
    String supplierId;
    String brandId;
    String storeId;
    String userMobileNumber;
    String staffMobileNumber;
    EnumValidYn settlementCompleteYn;
    Date settlementCompleteDate;
    SettlementTarget settlementTarget;
    SettlementMethodCode settlementMethodCode;
    Double listPrice;
    Double salesPrice;
    Double discountRate;
    Double discountAmount;
    Double discountAppliedAmount;
    Double settlementAmount;
    EnumValidYn vatIncludeYn;
    Double vatAmount;
    Double commissionRate;
    Double commissionAmount;
    Double sendCost;
    String settlementExceptReasonCode;
    String settlementExceptReason;
    Double remainBalance;
    Integer campaignId;
    String parentEv;
    SystemType system;
    Date activationDate;
}
