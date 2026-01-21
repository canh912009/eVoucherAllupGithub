package com.evoucher.adminapi.cms.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BrandWithCategorySearchResponse extends SearchBrandResponse {
    private List<String> categoryCode;
    private String supplierName;
}
