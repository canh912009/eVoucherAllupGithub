package com.evoucher.adminapi.admin.dao.models;

import com.evoucher.adminapi.admin.enums.OperatorRequestStatus;
import com.evoucher.adminapi.admin.service.models.search_response.OperatorSearchRes;
import com.evoucher.adminapi.auth.dao.models.Admin;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@SqlResultSetMapping(
        name = "operator_request_search_mapping",
        classes = @ConstructorResult(
                targetClass = OperatorSearchRes.class,
                columns = {
                        @ColumnResult(name = "requestId", type = Long.class),
                        @ColumnResult(name = "publishId", type = Long.class),
                        @ColumnResult(name = "publishName", type = String.class),
                        @ColumnResult(name = "customerId", type = String.class),
                        @ColumnResult(name = "customerName", type = String.class),
                        @ColumnResult(name = "ev", type = String.class),
                        @ColumnResult(name = "targetName", type = String.class),
                        @ColumnResult(name = "targetNumber", type = String.class),
                        @ColumnResult(name = "requestStatus", type = String.class),
                        @ColumnResult(name = "requestDate", type = Date.class)
                }
        )
)

@Entity
@Table(name = "tb_operator_request")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class OperatorRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "req_id")
    Long reqId;

    @Column(name = "ev", nullable = false, length = 40)
    String ev;

    @Column(name = "publish_id", nullable = false)
    Long publishId;

    @Column(name = "goods_id", nullable = false)
    Long goodsId;

    @Enumerated(EnumType.STRING)
    @Column(name = "req_status", nullable = false, length = 20, columnDefinition = "varchar(20) comment 'REQUESTED/APPROVED/REJECTED'")
    OperatorRequestStatus reqStatus;

    @Column(name = "requester", nullable = false, length = 20)
    @CreatedBy
    String requester;

    @Column(name = "req_dt", nullable = false)
    @CreatedDate
    Date reqDt;

    @Column(name = "memo")
    String memo;

    @Column(name = "appr_dt", columnDefinition = "datetime comment 'Approve/reject date'")
    Date approveDate;

    @Column(name = "appr_memo")
    String approveMemo;

    @Column(name = "approver", length = 20)
    String approver;


    //read only
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester", referencedColumnName = "admin_id", insertable = false, updatable = false)
    @JsonBackReference
    Admin requestedAdmin;
    //read only
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver", referencedColumnName = "admin_id", insertable = false, updatable = false)
    @JsonBackReference
    Admin approvedAdmin;

}
