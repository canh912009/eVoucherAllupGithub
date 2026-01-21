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
public class GoodsPartnerResponse {
    private Integer id;
    private String goodsName;
    private String brandName;
    private String supplierName;
    private EnumValidYn validYn;
}
