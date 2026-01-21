package com.evoucher.adminapi.settlement.dao.model;

import com.evoucher.adminapi.common.config.PropertyConverter;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.GoodsType;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.enums.SystemType;
import com.evoucher.adminapi.settlement.enums.SettlementLogType;
import com.evoucher.adminapi.settlement.enums.SettlementTarget;
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
@Table(name = "TB_SETTLEMENT_LOG")
public class SettlementLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    private Integer logId;
    @Column(name = "EV")
    private String ev;
    @Column(name = "SETTLEMENT_LOG_TYPE")
    @Enumerated(EnumType.STRING)
    private SettlementLogType settlementLogType;
    @Column(name = "PUBLISH_ID")
    private Integer publishId;
    @Column(name = "PUBLISH_DTL_ID")
    private Integer publishDetailId;
    @Column(name = "TRANSACTION_ID")
    private Integer transactionId;
    @Column(name = "TRANSACTION_DT")
    private Date transactionDate;
    @Column(name = "LOG_CREATE_DT")
    private Date logCreateDate;
    @Column(name = "VOUCHER_TYPE_CD")
    @Enumerated(EnumType.STRING)
    private GoodsType voucherTypeCode;
    @Column(name = "GOODS_ID")
    private Integer goodsId;
    @Column(name = "CUSTOMER_ID")
    private String customerId;
    @Column(name = "SUPPLIER_ID")
    private String supplierId;
    @Column(name = "BRAND_ID")
    private String brandId;
    @Column(name = "STORE_ID")
    private String storeId;
    @Column(name = "USER_MOBILE_NUM")
    @Convert(converter = PropertyConverter.class)
    private String userMobileNumber;
    @Column(name = "STAFF_MOBILE_NUM")
    @Convert(converter = PropertyConverter.class)
    private String staffMobileNumber;
    @Column(name = "SETTLEMENT_COMPLETE_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn settlementCompleteYn;
    @Column(name = "SETTLEMENT_COMPLETE_DT")
    private Date settlementCompleteDate;
    @Column(name = "SETTLEMENT_TARGET")
    @Enumerated(EnumType.STRING)
    private SettlementTarget settlementTarget;
    @Column(name = "SETTLEMENT_METHOD_CD")
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode settlementMethodCode;
    @Column(name = "LIST_PRICE")
    private Double listPrice;
    @Column(name = "SALES_PRICE")
    private Double salesPrice;
    @Column(name = "DC_RATE")
    private Double discountRate;
    @Column(name = "DC_AMOUNT")
    private Double discountAmount;
    @Column(name = "DC_APPLIED_AMOUNT")
    private Double discountAppliedAmount;
    @Column(name = "SETTLEMENT_AMOUNT")
    private Double settlementAmount;
    @Column(name = "VAT_INC_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn vatIncludeYn;
    @Column(name = "VAT_AMOUNT")
    private Double vatAmount;
    @Column(name = "COMMISSION_RATE")
    private Double commissionRate;
    @Column(name = "COMMISSION_AMOUNT")
    private Double commissionAmount;
    @Column(name = "SEND_COST")
    private Double sendCost;
    @Column(name = "SETTLEMENT_EXCEPT_REASON_CD")
    private String settlementExceptReasonCode;
    @Column(name = "SETTLEMENT_EXCEPT_REASON")
    private String settlementExceptReason;
    @Column(name = "REMAIN_BALANCE")
    private Double remainBalance;
    @Column(name = "CAMPAIGN_ID")
    private Double campaignId;
    @Column(name = "ev_system")
    @Enumerated(EnumType.STRING)
    private SystemType system;
}
