package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.ExchangeType;
import com.evoucher.evoucherbe.common.enums.VoucherTypeCode;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherExchangeHistoryDto {
    Integer id;
    ExchangeType exchangeType;
    Date transactionDate;
    String storeId;
    String ev;
    VoucherTypeCode voucherTypeCode;
    Integer goodsId;
    String goodsName;
    Double listPrice;
    Double discountRate;
    Double discountAmount;
    Double exchangeAmount;
    String userMobileNumber;
    String staffMobileNumber;
    private Integer usageRemainingCount;
}
