package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.models.BaseRegisEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_CAMPAIGN_APPRV_HISTORY")
public class CampaignApproveHistory extends BaseRegisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPRV_HISTORY_ID")
    private Integer id;

    @Column(name = "CAMPAIGN_ID")
    private Integer campaignId;

    @Column(name = "APPRV_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;

    @Column(name = "rejct_reason")
    private String rejectReason;
}
