package com.castis.publishservice.dto.response.watane;

import lombok.Data;

import java.util.List;

@Data
public class WataneTransactionResult {
    private int transactionStatus;
    private String transactionId;
    private List<WataneProduct> codes;
}
