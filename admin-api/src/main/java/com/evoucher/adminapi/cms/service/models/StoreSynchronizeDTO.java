package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSynchronizeDTO extends BaseDTO {

    private String id;
    private String storeName;
    private String storeImagePath;
    private String storeImageName;
    private String brandId;
    private String supplierId;
    private String validYn;
    private String mapCode;
    private String mapInteractionType;
    private String region;
    private String storeType;
    private String fullAddress;
    private String telephoneNumber;
}
