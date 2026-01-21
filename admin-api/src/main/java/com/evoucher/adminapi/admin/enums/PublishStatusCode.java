package com.evoucher.adminapi.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PublishStatusCode {
    WAIT_APPRV("WAIT_APPRV"),
    CANCEL("CANCEL"),
    APPROVED("APPROVED"),
    CANCEL_APPRV("CANCEL_APPRV"),
    REJECTED("REJECTED");

    private final String value;
}
