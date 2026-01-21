package asia.castis.evoucher.api.entity;

import asia.castis.evoucher.api.common.enums.ApproveStatus;
import asia.castis.evoucher.api.common.enums.EnumValidYn;
import asia.castis.evoucher.api.common.enums.SettlementMethodCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "TB_SUPPLIER")
public class Supplier extends BaseEntity {

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
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode settlementMethodCode;

    @Column(name = "SUPPLY_DC_RATE")
    private Double supplyDiscountRate;

    @Column(name = "SUPPLY_COMMISSION_RATE")
    private Double supplyCommissionRate;

    @Column(name = "VAT_INC_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn vatIncludeYn;

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
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "APPRV_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;

    @Column(name = "APPRVER_ID")
    private String approveId;
}
