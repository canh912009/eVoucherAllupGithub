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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_CUSTOMER_CONTRACT_APPRV_HISTORY")
@EntityListeners(AuditingEntityListener.class)
public class CustomerContractApproveHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPRV_HISTORY_ID")
    private Integer id;

    @Column(name = "CUSTOMER_CONTRACT_ID")
    private Integer customerContractId;

    @Column(name = "APPRV_STATUS_CD")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;

    @Column(name = "REJCT_REASON")
    private String rejectReason;

    @Column(name = "REG_ID", updatable = false)
    @CreatedBy
    private String regId;

    @Column(name = "REG_DT", updatable = false)
    @CreatedDate
    private Date regDt;
}
