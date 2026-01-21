package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SupplierDTO extends BaseDTO {
    private String id;
    private String taxcode;
    private String supplierName;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String settlementMethodCode;
    private Double supplyDiscountRate;
    private Double supplyCommissionRate;
    private String vatIncludeYn;
    private String managerName;
    private String managerEmail;
    private String managerMobileNumber;
    private String primaryContactName;
    private String primaryContactEmail;
    private String primaryContactMobile;
    private String validYn;
    private String approveStatusCode;
    private String approveId;
}
