package com.castis.publishservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "TB_SUPPLIER")
public class Supplier {

    @Id
    @Column(name = "supplier_id")
    private String id;

    @Column(name = "TAXCODE")
    private String taxcode;

    @Column(name = "SUPPLIER_NM")
    private String supplierName;

    @Column(name = "BANK_NM")
    private String bankName;

    @Column(name = "ACCOUNT_NUM")
    private String accountNumber;

    @Column(name = "ACCOUNT_NM")
    private String accountName;

    @Column(name = "SETTLEMENT_METHOD_CD")
    private String settlementMethodCode;

    @Column(name = "SUPPLY_DC_RATE")
    private Double supplyDiscountRate;

    @Column(name = "SUPPLY_COMMISSION_RATE")
    private Double supplyCommissionRate;

    @Column(name = "VAT_INC_YN")
    private String vatIncludeYn;

    @Column(name = "MNGR_NM")
    private String managerName;

    @Column(name = "MNGR_EMAIL")
    private String managerEmail;

    @Column(name = "MNGR_MOBILE_NO")
    private String managerMobileNumber;

    @Column(name = "PRIMARY_CONTACT_NM")
    private String primaryContactName;

    @Column(name = "PRIMARY_CONTACT_EMAIL")
    private String primaryContactEmail;

    @Column(name = "PRIMARY_CONTACT_MOBILE_NO")
    private String primaryContactMobile;

    @Column(name = "VALID_YN")
    private String validYn;

    @Column(name = "APPRV_STATUS_CD")
    private String approveStatusCode;

    @Column(name = "APPRVER_ID")
    private String approveId;@CreatedBy
    @Column(name = "REG_ID", updatable = false)
    private String regId;

    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private Date regDt;

    @LastModifiedBy
    @Column(name = "UPDT_ID")
    private String updtId;

    @LastModifiedDate
    @Column(name = "UPDT_DT")
    private Date updtDt;
}