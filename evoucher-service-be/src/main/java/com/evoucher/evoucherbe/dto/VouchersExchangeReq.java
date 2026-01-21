package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.McpExchangeType;
import com.evoucher.evoucherbe.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class VouchersExchangeReq {
    @DateTimeFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    @Setter
    private Date transactionDate;
    private String storeId;
    private List<String> voucherIds;
    @Setter
    private Double exchangeAmount;
    @Setter
    private String vnptReceiverPhoneNo;
    private String staffMobileNumber;
    McpExchangeType vnptExchangeType;
    String vnptProviderCode;
}
