package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.ApproveStatus;
import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.common.enums.SettlementMethodCode;
import com.evoucher.evoucherbe.common.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_SUPPLIER_CONTRACT")
public class SupplierContract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUPPLIER_CONTRACT_ID")
    private Integer id;

    @Column(name = "CONTRACT_NM")
    private String contractName;

    @Column(name = "ST_DT")
    private Date startDate;

    @Column(name = "ED_DT")
    private Date endDate;

    @Column(name = "SUPPLIER_ID")
    private String supplierId;

    @Column(name = "SUPPLY_DC_AMOUNT")
    private Double supplyDiscountAmount;

    @Column(name = "SUPPLY_DC_RATE")
    private Double supplyDiscountRate;

    @Column(name = "SUPPLY_COMMISSION_RATE")
    private Double supplyCommissionRate;

    @Column(name = "SUPPLY_VAT_INC_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn supplyVatIncludeYn;

    @Column(name = "SUPPLY_SETTLEMENT_METHOD_CD")
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode supplySettlementMethodCode;

    @Column(name = "APPRV_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;

    @Column(name = "APPRV_REQ_ID")
    private String approveRequestId;

    @Column(name = "APPRV_REQ_DT")
    private Date approveRequestDate;

    @Column(name = "APPRVER_ID")
    private String approveId;

    @Column(name = "APPRV_DT")
    private Date approveDate;

    @Column(name = "REJCTER_ID")
    private String rejectId;

    @Column(name = "REJCT_DT")
    private Date rejectDate;

    @Column(name = "REJCT_REASON")
    private String rejectReason;

    @Column(name = "CONTRACT_FILE_PATH")
    private String contractFilePath;

    @Column(name = "CONTRACT_FILE_NM")
    private String contractFileName;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;
}
