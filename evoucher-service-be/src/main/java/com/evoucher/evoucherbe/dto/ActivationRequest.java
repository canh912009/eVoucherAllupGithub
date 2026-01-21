package com.evoucher.evoucherbe.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ActivationRequest {
    String phoneNumber;
    String serialNumber;
    String ev;
}
