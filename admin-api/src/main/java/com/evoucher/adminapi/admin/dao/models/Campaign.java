package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.exception.CustomCodeException;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.evoucher.adminapi.common.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_CAMPAIGN")
public class Campaign extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAMPAIGN_ID")
    private Integer id;

    @Column(name = "CAMPAIGN_NM")
    private String campaignName;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "CUSTOMER_CONTRACT_ID")
    private Integer customerContractId;

    @Column(name = "ST_DT")
    private Date startDate;

    @Column(name = "ED_DT")
    private Date endDate;

    @Column(name = "MSG_SUBJECT")
    private String messageSubject;

    @Column(name = "MSG_CONTENT")
    private String messageContent;

    @Column(name = "MSG_CALLING_NUM")
    private String messageCallingNumber;

    @Column(name = "VALID_YN")
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

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

    @ManyToOne
    @JoinColumn(name="message_template_id")
    private MessageTemplate messageTemplate;

    @Column(name = "CONTENT_LINK")
    private String contentLink;

    @Column(name = "CONTENT_IMAGE_PATH")
    private String contentImagePath;

    @Column(name = "CONTENT_IMAGE_NAME")
    private String contentImageName;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "show_popup_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn showPopupYn;

//    public void setStartDate(LocalDate startDate) {
//        this.startDate = DateUtils.generateLocalDateToStartDay(startDate);
//    }
//
//    public void setEndDate(LocalDate endDate) {
//        this.endDate = DateUtils.generateLocalDateToEndDay(endDate);
//    }

    public void setApproveStatusInfo(ApproveStatus approveStatus) {
        if (Objects.isNull(approveStatus)) return;

        UserPrincipal user = LoggedInUserContext.getLoggedInUser();
        this.approveStatusCode = approveStatus;
        switch (approveStatus) {
            case REQ:
                this.approveRequestId = user.getId();
                this.approveRequestDate = new Date();
                break;
            case CANCEL_REQ:
                this.approveStatusCode = null;
                this.approveRequestId = null;
                this.approveRequestDate = null;
                break;
            case APPRV:
                this.approveId = user.getId();
                this.approveDate = new Date();
                break;
            case REJCT:
            case CANCEL_APPRV:
                break;
            default:
                throw new CustomCodeException(
                        MessageUtils.getMessage("evoucher.campaign.approveStatus.not.valid"),
                        HttpStatus.BAD_REQUEST);
        }
    }
}
