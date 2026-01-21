package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.VnptCardAction;
import com.evoucher.adminapi.common.enums.VnptProviderType;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class VnptProviderDto extends BaseDTO {
    @NotEmpty
    private String providerCd;
    private String topupProviderCd;
    @NotEmpty
    private String providerNm;
    @NotNull
    private VnptProviderType providerType;
    @NotNull
    private EnumValidYn validYn;
    private String allowedCardFaces;
    private VnptCardAction allowedActions;
}
