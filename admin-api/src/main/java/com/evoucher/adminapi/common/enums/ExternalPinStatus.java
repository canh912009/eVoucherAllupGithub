package com.evoucher.adminapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExternalPinStatus {
    AVAILABLE,
    USED,
    RESERVED
}
