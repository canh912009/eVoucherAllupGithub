package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.service.request.EndUserRequest;
import com.evoucher.evoucherbe.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class EVoucherTransferProcess {

    private String transferStatusCode;
    @JsonFormat(pattern= Constant.Common.COMMON_DATETIME_FORMAT)
    private Date transactionDate;
    @JsonFormat(pattern= Constant.Common.COMMON_DATETIME_FORMAT)
    private Date receiptConfirmDate;
    @JsonFormat(pattern= Constant.Common.COMMON_DATETIME_FORMAT)
    private Date returnDate;
    private String fromVoucherShortLink;
    private String fromEv;
    private String fromMobileNumber;
    private String toVoucherShortLink;
    private String toEv;
    private String toMobileNumber;
    private String voucherTypeCode;
    private Double initAmount;
    private Double transferAmount;
    private EndUserRequest endUser;
}
