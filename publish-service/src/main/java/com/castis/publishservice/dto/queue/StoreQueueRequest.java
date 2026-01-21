package com.castis.publishservice.dto.queue;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreQueueRequest {
    String storeId;
    String storeName;
    String storeImagePath;
    String storeImageName;
    String supplierId;
    String brandId;
    String validYN;
    String registerDate;
    String registerId;
    String updateDate;
    String region;
    String storeType;
    String tel;
    String mapCode;
    String mapInteractionType;
    String fullAddress;
    String updateId;
}
