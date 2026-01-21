package com.evoucher.externalserviceapi.service.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ExternalPublishResponse {
    private String transactionId;
    private List<OrderPinResponse> orders;
}
