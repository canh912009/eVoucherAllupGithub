package com.evoucher.evoucherbe.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_customer")
public class Customer  {
    @Id
    @Column(name = "customer_id")
    private String id;

    @Column(name = "customer_nm")
    private String customerName;

    @Column(name = "taxcode")
    private String taxcode;

    @Column(name = "bank_nm")
    private String bankName;

    @Column(name = "account_num")
    private String accountNumber;

    @Column(name = "account_nm")
    private String accountName;

    @Column(name = "mngr_nm")
    private String managerName;

    @Column(name = "mngr_email")
    private String managerEmail;

    @Column(name = "mngr_mobile_no")
    private String managerMobileNo;

    @Column(name = "valid_yn")
    private String validYn;

    @Column(name = "apprv_status_cd")
    private String approveStatusCode;

    @Column(name = "apprver_id")
    private String approveId;

    @Column(name = "customer_type")
    private String customerTypeCode;

    @CreatedBy
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
