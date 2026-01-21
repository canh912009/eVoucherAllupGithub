package com.evoucher.adminapi.cms.service.models.vnptEPay.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PurchaseRequest {
    @NotNull(message = "giftId can not be null")
    Long giftId;
    @NotNull(message = "quantity can not be null")
    Integer quantity;
}
