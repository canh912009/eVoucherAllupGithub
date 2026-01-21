package com.evoucher.evoucherbe.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExternalPinStatus {
    AVAILABLE,
    USED,
    RESERVED
}
