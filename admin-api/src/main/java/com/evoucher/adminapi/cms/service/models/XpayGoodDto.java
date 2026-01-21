package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.Data;

@Data
public class XpayGoodDto extends BaseDTO {
    private Long id;
    private Double faceValue;
    private EnumValidYn validYn;
    private String providerCode;
    private String description;
    private VnptProviderDto vnptProvider;
}
