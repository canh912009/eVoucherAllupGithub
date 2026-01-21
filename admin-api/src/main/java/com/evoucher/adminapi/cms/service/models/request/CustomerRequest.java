package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.auth.service.models.AdminDTO;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.CustomerType;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest extends BaseDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Id must contain only letters and numbers")
    @Size(max = 20, message = "Id less than 20 characters!")
    private String id;
    @NotBlank(message = "Customer name is empty!")
    private String customerName;
    @NotBlank(message = "Taxcode is empty!")
    @Size(max = 30, message = "Taxcode less than 30 characters!")
    private String taxcode;
    @NotBlank(message = "Bank name is empty!")
    private String bankName;
    @NotBlank(message = "Bank account number is empty!")
    private String accountNumber;
    @NotBlank(message = "Bank account name is empty!")
    private String accountName;
    private SettlementMethodCode settlementMethodCode;
    private Double sellDiscountRate;
    private Double sellCommissionRate;
    private EnumValidYn vatIncludeYn;
    private Double sendCost;
    @NotBlank(message = "Manager name is empty!")
    private String managerName;
    @NotBlank(message = "Manager email is empty!")
    @Email
    private String managerEmail;
    @NotBlank(message = "Manager mobile number is empty!")
    @Size(max = 20, message = "Manager mobile number less than 20 characters")
    private String managerMobileNo;
    private String primaryContactName;
    private String primaryContactEmail;
    private String primaryContactMobileNo;
    private ApproveStatus approveStatusCode;
    @NotNull(message = "Customer type is empty!")
    private CustomerType customerTypeCode;
    @NotNull(message = "Representative Mobile Number is empty!")
    private String representativeMobile;
    @NotNull(message = "Representative E Mail is empty!")
    @Email
    private String representativeMail;
    private AdminDTO admin;
}
