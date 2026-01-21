package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.ExchangeType;
import com.evoucher.evoucherbe.common.enums.McpExchangeType;
import com.evoucher.evoucherbe.common.enums.VoucherStatusCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EVoucherHistoryProcess {
    private ExchangeType exchangeType;
    private String transactionDate;
    private String storeId;
    @JsonProperty("voucherId")
    private String ev;
    private String voucherTypeCode;
    private Integer goodsId;
    private String goodsName;
    @Setter
    private Double listPrice;
    private Double discountRate;
    private Double discountAmount;
    @Setter
    private Double exchangeAmount;
    @Setter
    private String vnptReceiverPhoneNo;
    private String staffMobileNumber;
    private VoucherStatusCode voucherStatusCode;
    private Double balance;
    McpExchangeType vnptExchangeType;
    String vnptProviderCode;
}
