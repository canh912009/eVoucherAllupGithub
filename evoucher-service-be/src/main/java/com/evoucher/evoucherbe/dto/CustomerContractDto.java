package com.evoucher.evoucherbe.dto;

import com.evoucher.evoucherbe.common.enums.ApproveStatus;
import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.common.enums.SettlementMethodCode;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerContractDto {
    private Integer id;
    private String contractName;
    private Date startDate;
    private Date endDate;
    private String customerId;
    private Double sellDiscountAmount;
    private Double sellDiscountRate;
    private Double sellCommissionRate;
    private EnumValidYn sellVatIncludeYn;
    private SettlementMethodCode sellSettlementMethodCode;
    private ApproveStatus approveStatusCode;
    private String approveRequestId;
    private Date approveRequestDate;
    private String approveId;
    private Date approveDate;
    private String rejectId;
    private Date rejectDate;
    private String rejectReason;
    private String contractFilePath;
    private String contractFileName;
    private EnumValidYn validYn;
}
