package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.ApproveStatus;
import com.evoucher.evoucherbe.common.enums.EnumValidYn;
import com.evoucher.evoucherbe.common.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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
}
