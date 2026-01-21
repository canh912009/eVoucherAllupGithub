package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SupplierRequest extends BaseDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Id must contain only letters and numbers")
    @Size(max = 20, message = "Id less than 20 characters!")
    private String id;
    @NotBlank(message = "Taxcode is empty!")
    @Size(max = 30, message = "Taxcode less than 30 characters!")
    private String taxcode;
    @NotBlank(message = "Supplier name is empty!")
    private String supplierName;
    @NotBlank(message = "Bank name is empty!")
    private String bankName;
    @NotBlank(message = "Bank account is empty!")
    private String accountNumber;
    @NotBlank(message = "Account holder is empty!")
    private String accountName;
    @NotNull(message = "Settlement method is empty!")
    private SettlementMethodCode settlementMethodCode;
    @NotNull(message = "Discount rate is empty!")
    private Double supplyDiscountRate;
    @NotNull(message = "Commission rate is empty!")
    private Double supplyCommissionRate;
    @NotNull(message = "Including VAT is empty!")
    private EnumValidYn vatIncludeYn;
    @NotBlank(message = "Manager name is empty!")
    private String managerName;
    @NotBlank(message = "Manager email is empty!")
    private String managerEmail;
    @NotBlank(message = "Manager phone number is empty!")
    @Size(max = 20, message = "Manager mobile number less than 20 characters")
    private String managerMobileNumber;
    @NotBlank(message = "Primary contact name is empty!")
    private String primaryContactName;
    @NotBlank(message = "Primary contact email is empty!")
    private String primaryContactEmail;
    @NotBlank(message = "Primary contact mobile number is empty!")
    @Size(max = 20, message = "Primary contact mobile number less than 20 characters")
    private String primaryContactMobile;
    private ApproveStatus approveStatusCode;
}
