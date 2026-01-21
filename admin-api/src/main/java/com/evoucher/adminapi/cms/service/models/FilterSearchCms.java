package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SystemType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class FilterSearchCms {

    // supplier
    private String supplierId;
    private String taxcode;
    private String supplierName;

    // brand
    private String brandId;
    private String brandName;

    // store
    private String storeId;
    private String storeName;
    private String region;

    private String goodsId;
    private String goodsName;
    private String customerId;
    private String customerName;
    private String customerTypeCode;

    private String userMobileNum;
    private String userName;

    private String categoryCode;
    private String categoryName;

    private String approveStatusCode;
    private SystemType system;
    private List<SystemType> systems;
    private EnumValidYn validYn;
    private EnumValidYn isExpired;
    private Integer page;
    private Integer pageSize;

    private String providerCode;
    private String providerName;


}
