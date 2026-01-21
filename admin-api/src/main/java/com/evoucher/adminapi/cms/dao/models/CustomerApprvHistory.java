package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.ApproveStatus;
import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_customer_apprv_history")
public class CustomerApprvHistory extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "apprv_history_id")
    private Integer id;
    @Column(name = "customer_id")
    private String customerId;
    @Column(name = "apprv_status_cd")
    @Enumerated(EnumType.STRING)
    private ApproveStatus approveStatusCode;
    @Column(name = "rejct_reason")
    private String rejectReason;

}
