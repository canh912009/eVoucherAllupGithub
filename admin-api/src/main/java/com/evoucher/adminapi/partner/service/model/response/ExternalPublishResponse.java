package com.evoucher.adminapi.partner.service.model.response;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalPublishResponse {
    private String transactionId;
    private List<OrderPinResponse> orders;
}
