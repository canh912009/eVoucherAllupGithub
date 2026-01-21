package com.castis.publishservice.entity;

import com.castis.publishservice.utils.status.CompletedStatusCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_resend_history")
public class VoucherResendHistory extends BaseRegisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "EV")
    private String ev;

    @Column(name = "publish_id")
    private Long publishId;

    @Column(name = "publish_dtl_id")
    private Long publishDetailId;

    @Column(name = "prev_sms_id")
    private String previousSmsId;

    @Column(name = "memo")
    private String memo;

    @Column(name = "be_prev_publish_dtl_status_cd")
    private String backEndPreviousPublishDetailStatusCode;

    @Column(name = "be_update_result")
    @Enumerated(value = EnumType.STRING)
    private CompletedStatusCode backEndUpdateResult;

    @Column(name = "fe_prev_publish_dtl_status_cd")
    private String frontEndPreviousPublishDetailStatusCode;

    @Column(name = "fe_update_result")
    @Enumerated(value = EnumType.STRING)
    private CompletedStatusCode frontEndUpdateResult;
}
