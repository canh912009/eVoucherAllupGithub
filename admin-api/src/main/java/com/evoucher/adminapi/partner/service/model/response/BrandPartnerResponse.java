package com.evoucher.adminapi.partner.service.model.response;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandPartnerResponse {
    private String id;
    private String brandName;
    private EnumValidYn validYn;
    private String brandImagePath;
    private String brandImageName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SupplierPartnerResponse supplier;
}
