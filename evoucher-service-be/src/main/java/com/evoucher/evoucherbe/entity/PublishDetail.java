package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.config.PropertyConverter;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TB_PUBLISH_DETAIL")
public class PublishDetail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PUBLISH_DTL_ID")
    private Integer id;
    @Column(name = "PUBLISH_ID")
    private Integer publishId;
    @Convert(converter = PropertyConverter.class)
    @Column(name = "RECEIVER_MOBILE_NO")
    private String receiverMobileNo;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "PUBLISH_DTL_STATUS_CD")
    private String publishStatusCd;
    @Column(name = "PUBLISH_RSLT_MSG")
    private String publishResultMessage;
    @Column(name = "SMS_ID")
    private String smsId;
    @Column(name = "SMS_TYPE")
    private String smsType;
    @Column(name = "SMS_SEND_DT")
    private Date smsSendDt;
    @Column(name = "SMS_SEND_RSLT_DT")
    private Date smsSendResultDate;
    @Column(name = "EXT_PIN_ID")
    private Long externalPinId;
    @Column(name = "REG_DT")
    private Date regDt;
    @Column(name = "UPDT_DT")
    private Date updateDate;
}