package com.evoucher.adminapi.cms.service.models;

import com.evoucher.adminapi.common.models.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BrandDTO extends BaseDTO {
    private String id;
    private String brandName;
    private String brandImagePath;
    private String brandImageName;
    private String brandLogoPath;
    private String brandLogoName;
    private String description;
    private SupplierDTO supplier;
    private String validYn;
    private String defaultBrandYn;
    private List<GoodsDTO> listGoods;
    private List<StoreDTO> stores;
    private String displayType;
    private String system;
    private String brandCode;

    private String encryptionKey;
    private String authenticationKey;
    private String appId;
    private String serialNumberPrefix;
    private Integer serialNumberTotalLength;
    private String ipWhiteList;

}
