package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.McpExchangeType;
import com.evoucher.evoucherbe.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoucherExchangeReq {
    @DateTimeFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    @Setter
    private Date transactionDate;
    private String storeId;
    private String voucherId;
    @Setter
    private Double exchangeAmount;
    private String staffMobileNumber;

    @Setter
    private String vnptReceiverPhoneNo;
    McpExchangeType vnptExchangeType;
    String vnptProviderCode;

    @Setter
    private String xpayReceiverPhoneNo;
    McpExchangeType xpayExchangeType;
    String xpayProviderCode;
}
