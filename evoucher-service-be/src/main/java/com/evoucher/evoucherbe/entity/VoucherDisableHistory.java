package com.evoucher.evoucherbe.entity;

import com.evoucher.evoucherbe.common.enums.CompletedStatusCode;
import com.evoucher.evoucherbe.common.enums.VoucherStatusCode;
import com.evoucher.evoucherbe.common.models.BaseRegisEntity;
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
@Table(name = "TB_DISABLE_HISTORY")
public class VoucherDisableHistory extends BaseRegisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EV")
    private String ev;

    @Column(name = "MEMO")
    private String memo;

    @Column(name = "BE_PREV_STATUS_CD")
    @Enumerated(value = EnumType.STRING)
    private VoucherStatusCode backEndPreviousStatusCode;

    @Column(name = "BE_UPDATE_RESULT")
    @Enumerated(value = EnumType.STRING)
    private CompletedStatusCode backEndUpdateResult;

    @Column(name = "FE_PREV_STATUS_CD")
    @Enumerated(value = EnumType.STRING)
    private VoucherStatusCode frontEndPreviousStatusCode;

    @Column(name = "FE_UPDATE_RESULT")
    @Enumerated(value = EnumType.STRING)
    private CompletedStatusCode frontEndUpdateResult;
}
