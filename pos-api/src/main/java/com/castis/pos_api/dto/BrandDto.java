package com.castis.pos_api.dto;

import com.castis.pos_api.enum_constant.EnumValidYn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BrandDto {
    private String id;
    private String brandName;
    private String brandImagePath;
    private String brandImageName;
    private String description;
    private String supplierId;
    private EnumValidYn validYn;
    private String defaultBrandYn;
    private String isPosLink;
    private String system;
    private String brandCode;
    private String authenticationKey;
    private String encryptionKey;
    private String serialNumberPrefix;
    private int serialNumberTotalLength;
    private String ipWhiteList;
}
