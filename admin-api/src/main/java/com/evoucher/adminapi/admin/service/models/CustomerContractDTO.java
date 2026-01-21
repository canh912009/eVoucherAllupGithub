package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.cms.service.models.CustomerDTO;
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
public class CustomerContractDTO extends BaseDTO {
    private Integer id;
    private String contractName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private CustomerDTO customer;
    private Double sellDiscountAmount;
    private Double sellDiscountRate;
    private Double sellCommissionRate;
    private EnumValidYn sellVatIncludeYn;
    private SettlementMethodCode sellSettlementMethodCode;
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
