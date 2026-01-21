package com.evoucher.evoucherbe.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDto {
    private String id;
    private String customerName;
    private String taxcode;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String managerName;
    private String managerEmail;
    private String managerMobileNo;
    private String validYn;
    private String approveStatusCode;
    private String approveId;
    private String customerTypeCode;
    private String regId;
    private Date regDt;
    private String updtId;
    private Date updtDt;
}
