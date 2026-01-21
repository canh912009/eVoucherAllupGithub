package com.evoucher.adminapi.cms.dao.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.VnptCardAction;
import com.evoucher.adminapi.common.enums.VnptProviderType;
import com.evoucher.adminapi.common.models.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_vnpt_epay_providers")
@Data
public class VnptProvider extends BaseEntity {

    @Id
    @Column(name = "provider_cd", nullable = false, length = 50)
    private String providerCd;

    @Column(name = "topup_provider_cd", nullable = false, length = 50)
    private String topupProviderCd;

    @Column(name = "provider_nm", nullable = false, length = 100)
    private String providerNm;

    @Column(name = "provider_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private VnptProviderType providerType;

    @Column(name = "valid_yn", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumValidYn validYn;

    @Column(name = "allowed_card_faces", nullable = false)
    private String allowedCardFaces;

    @Enumerated(EnumType.STRING)
    @Column(name = "allowed_actions", length = 100)
    private VnptCardAction allowedActions;
}
