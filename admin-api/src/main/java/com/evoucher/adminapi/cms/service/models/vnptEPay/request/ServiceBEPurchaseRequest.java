package com.evoucher.adminapi.cms.service.models.vnptEPay.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceBEPurchaseRequest {
    String requestId;
    String provider;
    Integer amount;
    Integer quantity;
}
