package com.castis.publishservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandDTO {
    private String id;
    private String brandName;
    private String brandImagePath;
    private String brandImageName;
    private String description;
    private String supplierId;
    private String validYn;
    private String defaultBrandYn;
    private String regId;
    private Date regDt;
    private String updateId;
    private Date updateDate;
    private String isPosLink;
    private String displayType;
    private String system;
}
