package com.evoucher.adminapi.admin.service.models;

import com.evoucher.adminapi.common.serializers.ToEncryptedFieldDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
@Builder
public class FilterSearchAdmin {

    private String contractId;
    private String contractName;

    private String customerId;
    private String customerName;

    private String supplierId;
    private String supplierName;

    private String goodsName;

    private String campaignId;
    private String campaignName;

    private String publishId;
    private String publishName;

    private String approveStatusCode;
    private String statusCode;
    private String smsType;

    private String requestStatus;
    private String targetName;
    private String targetNumber;
    private String ev;

    private String requestType;

    private Integer page;
    private Integer pageSize;
}
