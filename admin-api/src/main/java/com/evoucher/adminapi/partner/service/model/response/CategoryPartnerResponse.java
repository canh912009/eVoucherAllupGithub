package com.evoucher.adminapi.partner.service.model.response;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPartnerResponse {
    private String categoryCode;
    private String categoryName;
    private EnumValidYn validYn;
}
