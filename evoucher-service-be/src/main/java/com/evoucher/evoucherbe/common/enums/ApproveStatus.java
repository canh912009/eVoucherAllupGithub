package com.evoucher.evoucherbe.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApproveStatus {
    REQ,
    CANCEL_REQ,
    APPRV,
    REJCT,
    CANCEL_APPRV
}
