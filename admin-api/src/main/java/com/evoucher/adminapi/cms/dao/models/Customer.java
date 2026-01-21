package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.CustomerType;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_customer")
public class Customer extends BaseEntity {
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
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "apprv_status_cd")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;

    @Column(name = "apprver_id")
    private String approveId;

    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerTypeCode;

    @Column(name = "drop_sell_dc_rate")
    private Double sellDiscountRate;

    @Column(name = "drop_sell_commission_rate")
    private Double sellCommissionRate;

    @Column(name = "drop_vat_inc_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn vatIncludeYn;

    @Column(name = "drop_settlement_method_cd")
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode settlementMethodCode;

    @Column(name = "drop_send_cost")
    private Double sendCost;

    @Column(name = "drop_primary_contact_nm")
    private String primaryContactName;

    @Column(name = "drop_primary_contact_email")
    private String primaryContactEmail;

    @Column(name = "drop_primary_contact_mobile_no")
    private String primaryContactMobileNo;

    @Column(name = "representative_mobile_no")
    private String representativeMobile;

    @Column(name = "representative_email")
    private String representativeMail;

    @Column(name = "admin_id")
    private String adminId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", referencedColumnName = "admin_id", insertable = false, updatable = false)
    @JsonBackReference
    Admin admin;
}
