package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TB_PUBLISH")
public class Publish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publish_id")
    private Integer id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id")
    private Campaign campaign;
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
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "supplier_id", referencedColumnName = "supplier_id")
    private Supplier supplier;
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
    private String publishStatusCode;
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
    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private Date regDt;
    @LastModifiedDate
    @Column(name = "UPDT_DT")
    private Date updtDt;
    @Column(name = "CONTENT_LINK")
    private String contentLink;
    @Column(name = "CONTENT_IMAGE_PATH")
    private String contentImagePath;
    @Column(name = "CONTENT_IMAGE_NAME")
    private String contentImageName;
}
