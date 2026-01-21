package com.evoucher.externalserviceapi.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_external_publish")
public class ExternalPublish {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "sha")
    private String sha;
    @Column(name = "request_time")
    private Date requestTime;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "is_send_sms")
    private Boolean isSendSms;
    @Column(name = "sms_scheduled")
    private Date smsSchedule;
    @Column(name = "subject")
    private String subject;
    @Column(name = "content_text")
    private String contentText;
    @Column(name = "content_image_path")
    private String contentImagePath;
    @Column(name = "content_image_name")
    private String contentImageName;
    @Column(name = "content_link")
    private String contentLink;
    @Column(name = "orders")
    private String orders;
}
