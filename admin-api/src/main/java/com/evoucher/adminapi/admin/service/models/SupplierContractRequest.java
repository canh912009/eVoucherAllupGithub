package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.serializer.EndDateWithoutTimeDeserializer;
import com.evoucher.adminapi.serializer.StartDateWithoutTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierContractRequest {
    @NotBlank(message = "Contract name is empty!")
    private String contractName;

    @NotNull(message = "Contract start date is empty!")
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = StartDateWithoutTimeDeserializer.class)
    private Date startDate;

    @NotNull(message = "Contract end date is empty!")
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = EndDateWithoutTimeDeserializer.class)
    private Date endDate;
    @NotBlank(message = "Customer ID is empty!")
    private String supplierId;
    @PositiveOrZero(message = "Supply discount amount must be zero or positive")
    private Double supplyDiscountAmount;
    @PositiveOrZero(message = "Supply discount rate must be zero or positive")
    private Double supplyDiscountRate;
    @PositiveOrZero(message = "Supply commission rate must be zero or positive")
    private Double supplyCommissionRate;
    private SettlementMethodCode supplySettlementMethodCode;
    private EnumValidYn supplyVatIncludeYn;
    private String contractFilePath;
    private String contractFileName;
    private ApproveStatus approveStatusCode;
}
