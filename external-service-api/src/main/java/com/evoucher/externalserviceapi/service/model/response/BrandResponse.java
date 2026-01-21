package com.evoucher.externalserviceapi.service.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BrandResponse {
    private String id;
    private String brandName;
    private String validYn;
    private String brandImagePath;
    private String brandImageName;
    private SupplierResponse supplier;
}
