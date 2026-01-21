package com.evoucher.adminapi.partner.service.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPinRequest {
    private Integer goodsId;
    private String userPhoneNo;
    private String userName;
}
