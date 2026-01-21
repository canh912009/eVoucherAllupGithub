package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceBEPurchaseResponse {
    List<VnptCard> listCards;
}
