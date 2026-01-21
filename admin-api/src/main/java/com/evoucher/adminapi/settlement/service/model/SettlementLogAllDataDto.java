package com.evoucher.adminapi.settlement.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementLogAllDataDto extends SettlementLogDTO {
    private Integer remainingCount;
    private String otp;
    private String system;
    private String transactionId;
    private Long faceValue;
    private String cardSerial;
    private String cardPin;
    private String topupNumber;
    private String provider;
    private String requestId;
    private String password;
}
