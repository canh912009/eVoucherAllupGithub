package com.castis.pos_api.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionIdOnlyResponse {
    String transactionId;
    String responseTime;
    public TransactionIdOnlyResponse(String transactionId) {
        this.transactionId = transactionId;
    }
}
