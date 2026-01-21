package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SearchBrandResponse extends BaseDTO {
    private String id;
    private String brandName;
    private String supplierId;
    private String supplierName;
    private String validYn;
    private String defaultBrandYn;
    private String system;
    private String brandCode;
    private String displayType;
}
