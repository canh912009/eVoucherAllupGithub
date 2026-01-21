package com.castis.publishservice.entity;

import com.castis.publishservice.converter.PropertyConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Data
@Table(name = "tb_publish_detail")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishDetail {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "publish_dtl_id")
    private Long publishDtlId;
    @Basic
    @Column(name = "reg_dt")
    @CreatedDate
    private Date regDt;
    @Basic
    @Column(name = "publish_id")
    private long publishId;
    @Basic
    @Column(name = "user_id")
    private Long userId;
    @Basic
    @Convert(converter = PropertyConverter.class)
    @Column(name = "receiver_mobile_no")
    private String receiverMobileNo;
    @Basic
    @Column(name = "publish_dtl_status_cd")
    private String publishStatusCd;
    @Basic
    @Column(name = "publish_rslt_msg")
    private String publishResultMessage;
    @Basic
    @Column(name = "updt_dt")
    @LastModifiedDate
    private Date updateDate;
    @Basic
    @Column(name = "sms_id")
    private String smsId;
    @Basic
    @Column(name = "sms_type")
    private String smsType;
    @Basic
    @Column(name = "sms_send_dt")
    private Date smsSendDt;
    @Basic
    @Column(name = "sms_send_rslt_dt")
    private Date smsSendResultDate;
    @Basic
    @Column(name = "ext_pin_id", nullable = true)
    private Long extPinId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PublishDetail that = (PublishDetail) o;
        return publishDtlId == that.publishDtlId && publishId == that.publishId && Objects.equals(regDt, that.regDt) && Objects.equals(receiverMobileNo, that.receiverMobileNo) && Objects.equals(publishStatusCd, that.publishStatusCd) && Objects.equals(publishResultMessage, that.publishResultMessage) && Objects.equals(updateDate, that.updateDate) && Objects.equals(smsId, that.smsId) && Objects.equals(smsType, that.smsType) && Objects.equals(smsSendDt, that.smsSendDt) && Objects.equals(smsSendResultDate, that.smsSendResultDate) && Objects.equals(extPinId, that.extPinId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publishDtlId, regDt, publishId, receiverMobileNo, publishStatusCd, publishResultMessage, updateDate, smsId, smsType, smsSendDt, smsSendResultDate, extPinId);
    }
}
