package com.evoucher.adminapi.partner.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalPublishOrderPinResponse {
    private String transactionId;
    private OrderPinResponse order;
}
