package com.castis.publishservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
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
    private String regId;
    private String regDt;
    private String updtId;
    private String updtDt;
}