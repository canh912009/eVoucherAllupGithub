package com.evoucher.adminapi.cms.service.models.request;

import com.evoucher.adminapi.common.enums.EnumValidYn;
import com.evoucher.adminapi.common.enums.SystemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandRequest {
    @NotBlank(message = "Supplier is empty")
    private String supplierId;
    @NotBlank(message = "Brand company name is empty!")
    private String brandName;

    @NotBlank(message = "Brand image path is empty!")
    private String brandImagePath;
    @NotBlank(message = "Brand image name is empty!")
    private String brandImageName;

    private String brandLogoPath;
    private String brandLogoName;

    @NotBlank(message = "Brand infomation is empty!")
    private String description;
    @NotNull(message = "Active status is empty!")
    private EnumValidYn validYn;
    private EnumValidYn defaultBrandYn;
    @NotNull(message = "Display type is empty!")
    private String displayType;
    @NotNull(message = "System type is empty!")
    private SystemType system;
    private String brandCode;

    private String appId;
    private String serialNumberPrefix;
    private Integer serialNumberTotalLength;
    private String ipWhiteList;
}
