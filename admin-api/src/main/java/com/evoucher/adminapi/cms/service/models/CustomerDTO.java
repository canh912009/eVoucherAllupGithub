package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.auth.service.models.AdminDTO;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomerDTO extends BaseDTO {
    private String id;
    private String customerName;
    private String taxcode;
    private String bankName;
    private String accountNumber;
    private String accountName;
//    private Double sellDiscountRate;
//    private Double sellCommissionRate;
//    private String vatIncludeYn;
//    private String settlementMethodCode;
//    private Double sendCost;
    private String managerName;
    private String managerEmail;
    private String managerMobileNo;
//    private String primaryContactName;
//    private String primaryContactEmail;
//    private String primaryContactMobileNo;
    private String validYn;
    private String approveStatusCode;
    private String approverId;
    private String customerTypeCode;
    private String representativeMobile;
    private String representativeMail;
    private AdminDTO admin;
}
