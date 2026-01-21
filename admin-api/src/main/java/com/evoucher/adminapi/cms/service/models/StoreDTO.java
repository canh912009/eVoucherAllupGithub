package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.cms.dao.models.Brand;
import com.evoucher.adminapi.cms.dao.models.Supplier;
import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class StoreDTO extends BaseDTO {
    private String id;
    private String storeName;
    private String storeImagePath;
    private String storeImageName;
    private BrandDTO brand;
    private SupplierDTO supplier;
    private String validYn;
    private String mapCode;
    private String latitude;
    private String longitude;
    private String mapInteractionType;
    private String region;
    private String storeType;
    private String fullAddress;
    private String telephoneNumber;
    private String storeCode;

    public void setBrand(Brand brand) {
        this.brand = BrandDTO.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .build();
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = SupplierDTO.builder()
                .id(supplier.getId())
                .supplierName(supplier.getSupplierName())
                .build();
    }
}
