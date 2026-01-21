package com.evoucher.partner.service.bean.dtos;

import com.evoucher.partner.service.bean.enum_type.EnumValidYn;
import com.evoucher.partner.service.bean.enum_type.VnptCardAction;
import com.evoucher.partner.service.bean.enum_type.VnptProviderType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnptProviderDto extends BaseDTO {
    private String providerCd;
    private String topupProviderCd;
    private String providerNm;
    private VnptProviderType providerType;
    private EnumValidYn validYn;
    private String allowedCardFaces;
    private VnptCardAction allowedActions;
}
