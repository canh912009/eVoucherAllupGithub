package com.evoucher.externalserviceapi.service.model.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsResponse {
    private Integer id;
    private String goodsName;
    private String brandName;
    private String supplierName;
    private String validYn;
}
