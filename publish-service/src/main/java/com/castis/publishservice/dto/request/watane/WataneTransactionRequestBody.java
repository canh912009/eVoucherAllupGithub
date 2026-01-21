package com.castis.publishservice.dto.request.watane;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WataneTransactionRequestBody implements WataneRequestBody {
    private String requestTime;
    private String transactionId;
}
