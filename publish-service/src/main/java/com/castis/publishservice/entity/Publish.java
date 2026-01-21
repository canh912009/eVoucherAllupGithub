package com.castis.publishservice.entity;

import com.castis.publishservice.utils.status.PublishStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "tb_publish")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publish_id")
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "campaign_id", referencedColumnName = "campaign_id")
    Campaign campaign;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "goods_id", referencedColumnName = "goods_id")
    Goods good;
    @Column(name = "publish_nm")
    private String publishName;
    @Column(name = "msg_subject")
    private String messageSubject;
    @Column(name = "msg_content")
    private String messageContent;
    @Column(name = "msg_calling_num")
    private String messageCallingNumber;
    @Column(name = "booking_yn")
    private String bookingYn;
    @Column(name = "booking_dt")
    private Date bookingDate;
    @Column(name = "test_send_yn")
    private String testSendYn;
    @Column(name = "receiver_no_dupl_allow_yn")
    private String receiverNoDuplicateAllowYn;
    @Column(name = "upload_type")
    private String uploadType;
    @Column(name = "upload_file_path")
    private String uploadFilePath;
    @Column(name = "upload_file_nm")
    private String uploadFileName;
    @Column(name = "upload_text")
    private String uploadText;
    @Column(name = "sms_type")
    private String smsType;
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
    private String sellVatIncludeYn;
    @Column(name = "sell_settlement_method_cd")
    private String sellSettlementMethodCode;
    @Column(name = "send_cost")
    private Double sendCost;
    @Column(name = "publish_dt")
    private Date publishDate;
    @Column(name = "cancel_dt")
    private Date cancelDate;
    @Column(name = "publish_status_cd")
    @Enumerated(EnumType.STRING)
    private PublishStatus publishStatusCode;
    @Column(name = "apprv_status_cd")
    private String approveStatusCode;
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
    @Column(name = "REG_DT", updatable = false)
    private Date regDt;
    @LastModifiedDate
    @Column(name = "UPDT_DT")
    private Date updtDt;
    @Column(name = "sender_name")
    private String senderName;
}
