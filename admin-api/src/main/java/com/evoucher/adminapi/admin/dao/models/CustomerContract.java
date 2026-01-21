package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_CUSTOMER_CONTRACT")
public class CustomerContract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_CONTRACT_ID")
    private Integer id;

    @Column(name = "CONTRACT_NM")
    private String contractName;

    @Column(name = "ST_DT")
    private Date startDate;

    @Column(name = "ED_DT")
    private Date endDate;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "SELL_DC_AMOUNT")
    private Double sellDiscountAmount;

    @Column(name = "SELL_DC_RATE")
    private Double sellDiscountRate;

    @Column(name = "SELL_COMMISSION_RATE")
    private Double sellCommissionRate;

    @Column(name = "SELL_VAT_INC_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn sellVatIncludeYn;

    @Column(name = "SELL_SETTLEMENT_METHOD_CD")
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode sellSettlementMethodCode;

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

    public void setApproveStatusInfo(ApproveStatus approveStatus, String rejectReason) {
        if (Objects.isNull(approveStatus)) return;

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        this.setApproveStatusCode(approveStatus);
        switch (approveStatus) {
            case REQ: {
                this.setApproveRequestId(user.getId());
                this.setApproveRequestDate(new Date());
                break;
            }
            case APPRV: {
                this.setApproveId(user.getId());
                this.setApproveDate(new Date());
                break;
            }
            case REJCT: {
                this.setRejectId(user.getId());
                this.setRejectDate(new Date());
                this.setRejectReason(rejectReason);
                break;
            }
            default:
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.contract.approve.status.code.not.valid"),
                        HttpStatus.BAD_REQUEST);
        }

    }
}
