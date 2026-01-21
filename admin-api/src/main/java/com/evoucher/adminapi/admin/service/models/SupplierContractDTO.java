package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.cms.service.models.SupplierDTO;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierContractDTO extends BaseDTO {
    private Integer id;
    private String contractName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private SupplierDTO supplier;
    private Double supplyDiscountAmount;
    private Double supplyDiscountRate;
    private Double supplyCommissionRate;
    private EnumValidYn supplyVatIncludeYn;
    private SettlementMethodCode supplySettlementMethodCode;
    private ApproveStatus approveStatusCode;
    private String approveRequestId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveRequestDate;
    private String approveId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveDate;
    private String rejectId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date rejectDate;
    private String rejectReason;
    private String contractFilePath;
    private String contractFileName;
    private EnumValidYn validYn;
}
