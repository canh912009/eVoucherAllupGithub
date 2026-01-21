package com.castis.publishservice.dto.request.watane;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WatanePurchaseRequestBody implements WataneRequestBody {
    private String requestTime;
    private String transactionId;
    private String message;
    private List<AdditionalInfo> additionalInfo;
    private String code;
    private int quantity;
}
