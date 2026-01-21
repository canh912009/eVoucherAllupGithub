package com.castis.publishservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TB_CAMPAIGN")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAMPAIGN_ID")
    private Long id;

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
    private String validYn;

    @Column(name = "APPRV_STATUS_CD")
    private String campaignStatusCode;

    @Column(name = "APPRV_REQ_ID")
    private String approveRequestId;

    @Column(name = "APPRV_REQ_DT")
    private Date approveRequestDate;

    @Column(name = "APPRVER_ID")
    private String approveId;

    @Column(name = "APPRV_DT")
    private Date approveDate;
    @CreatedBy
    @Column(name = "REG_ID", updatable = false)
    private String regId;

    @CreatedDate
    @Column(name = "REG_DT", updatable = false)
    private Date regDt;

    @LastModifiedBy
    @Column(name = "UPDT_ID")
    private String updtId;

    @LastModifiedDate
    @Column(name = "UPDT_DT")
    private Date updtDt;

    @ManyToOne
    @JoinColumn(name="message_template_id")
    private MessageTemplate messageTemplate;
}
