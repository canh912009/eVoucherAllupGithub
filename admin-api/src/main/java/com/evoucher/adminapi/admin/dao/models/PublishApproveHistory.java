package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.common.enums.ApproveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_PUBLISH_APPRV_HISTORY")
@EntityListeners(AuditingEntityListener.class)
public class PublishApproveHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLISH_APPRV_HISTORY_ID")
    private Integer id;

    @Column(name = "PUBLISH_ID")
    private Integer publishId;

    @Column(name = "APPRV_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;

    @Column(name = "REJCT_REASON")
    private String rejectReason;

    @Column(name = "reg_id", updatable = false)
    @CreatedBy
    private String regId;

    @Column(name = "reg_dt", updatable = false)
    @CreatedDate
    private Date regDt;
}
