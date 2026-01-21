package com.evoucher.adminapi.auth.service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchAdminResponse {
    private String id;
    private String adminName;
    private String email;
    private String mobileNumber;
    private String adminCorporationId;
    private String supplierName;
    private String brandName;
    private String storeName;
    private String customerName;
    private String adminCorporationName;
    private String roleCode;

    public void setAdminCorporationName(SearchAdminResponse adminCorporation) {
        if (Objects.nonNull(adminCorporation.getSupplierName())) {
            this.adminCorporationName = adminCorporation.getSupplierName();
        } else if (Objects.nonNull(adminCorporation.getStoreName())) {
            this.adminCorporationName = adminCorporation.getStoreName();
        } else if (Objects.nonNull(adminCorporation.getBrandName())) {
            this.adminCorporationName = adminCorporation.getBrandName();
        } else if (Objects.nonNull(adminCorporation.getCustomerName())) {
            this.adminCorporationName = adminCorporation.getCustomerName();
        } else {
            this.adminCorporationName = adminCorporation.getAdminCorporationName();
        }
    }
}
