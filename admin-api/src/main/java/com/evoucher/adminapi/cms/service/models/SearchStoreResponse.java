package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SearchStoreResponse extends BaseDTO {
    private String id;
    private String storeName;
    private String brandId;
    private String brandName;
    private String supplierId;
    private String supplierName;
    private String validYn;
    private String region;
}
