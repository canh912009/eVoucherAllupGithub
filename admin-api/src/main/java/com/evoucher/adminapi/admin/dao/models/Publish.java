package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.admin.enums.PublishStatusCode;
import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.auth.service.models.UserPrincipal;
import com.evoucher.adminapi.cms.dao.models.Customer;
import com.evoucher.adminapi.cms.dao.models.Goods;
import com.evoucher.adminapi.common.config.LoggedInUserContext;
import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.admin.enums.SMSType;
import com.evoucher.adminapi.admin.enums.UploadDataType;
import com.evoucher.adminapi.common.enums.SettlementMethodCode;
import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "TB_PUBLISH")
public class Publish extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publish_id")
    private Integer id;
    @Column(name = "campaign_id")
    private Integer campaignId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_id", referencedColumnName = "goods_id")
    private Goods goods;
    @Column(name = "publish_nm")
    private String publishName;
    @Column(name = "msg_subject")
    private String messageSubject;
    @Column(name = "msg_content")
    private String messageContent;
    @Column(name = "msg_calling_num")
    private String messageCallingNumber;
    @Column(name = "booking_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn bookingYn;
    @Column(name = "booking_dt")
    private Date bookingDate;
    @Column(name = "test_send_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn testSendYn;
    @Column(name = "receiver_no_dupl_allow_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn receiverNoDuplicateAllowYn;
    @Column(name = "upload_type")
    @Enumerated(EnumType.STRING)
    private UploadDataType uploadType;
    @Column(name = "upload_file_path")
    private String uploadFilePath;
    @Column(name = "upload_file_nm")
    private String uploadFileName;
    @Column(name = "upload_text")
    private String uploadText;
    @Column(name = "sms_type")
    @Enumerated(EnumType.STRING)
    private SMSType smsType;
    @Column(name = "supplier_id")
    private String supplierId;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "sell_price")
    private Double sellPrice;
    @Column(name = "sell_list_price")
    private Double sellListPrice;
    @Column(name = "sell_dc_rate")
    private Double sellDiscountRate;
    @Column(name = "sell_dc_amount")
    private Double sellDiscountAmount;
    @Column(name = "sell_commission_rate")
    private Double sellCommissionRate;
    @Column(name = "sell_vat_inc_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn sellVatIncludeYn;
    @Column(name = "sell_settlement_method_cd")
    @Enumerated(EnumType.STRING)
    private SettlementMethodCode sellSettlementMethodCode;
    @Column(name = "send_cost")
    private Double sendCost;
    @Column(name = "publish_dt")
    private Date publishDate;
    @Column(name = "cancel_dt")
    private Date cancelDate;
    @Column(name = "publish_status_cd")
    private String statusCode;
    @Column(name = "apprv_status_cd")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;
    @Column(name = "apprv_req_id")
    private String approveRequestId;
    @Column(name = "apprv_req_dt")
    private Date approveRequestDate;
    @Column(name = "apprver_id")
    private String approveId;
    @Column(name = "apprver_dt")
    private Date approveDate;
    @Column(name = "rejcter_id")
    private String rejectId;
    @Column(name = "rejct_dt")
    private Date rejectDate;
    @Column(name = "rejct_reason")
    private String rejectReason;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "content_link")
    private String contentLink;
    @Column(name = "content_image_path")
    private String contentImagePath;
    @Column(name = "content_image_name")
    private String contentImageName;
    @Column(name = "sender_name")
    private String senderName;
    @Column(name = "number_of_vouchers")
    private Integer numberOfVouchers;
    @Column(name = "show_popup_yn")
    @Enumerated(EnumType.STRING)
    private EnumValidYn showPopupYn;

    public void setPublishStatusInfo(ApproveStatus approveStatus, String rejectReason) {
        UserPrincipal user = LoggedInUserContext.getLoggedInUser();

        this.setApproveStatusCode(approveStatus);
        switch (approveStatus) {
            case REQ: {
                this.setApproveRequestId(user.getId());
                this.setApproveRequestDate(new Date());
                this.setStatusCode(PublishStatusCode.WAIT_APPRV.getValue());
                break;
            }
            case CANCEL_REQ: {
                this.setApproveRequestId(null);
                this.setApproveRequestDate(null);
                this.setStatusCode(PublishStatusCode.CANCEL.getValue());
                break;
            }
            case REJCT: {
                this.setRejectId(user.getId());
                this.setRejectDate(new Date());
                this.setRejectReason(rejectReason);
                this.setStatusCode(PublishStatusCode.REJECTED.getValue());
                break;
            }
            case APPRV: {
                this.setApproveId(user.getId());
                this.setApproveDate(new Date());
                this.setStatusCode(PublishStatusCode.APPROVED.getValue());
                break;
            }
            case CANCEL_APPRV: {
                this.setApproveId(null);
                this.setApproveDate(null);
                this.setStatusCode(PublishStatusCode.CANCEL_APPRV.getValue());
                this.setCancelDate(new Date());
                break;
            }
            default:
                break;
        }
    }
}
