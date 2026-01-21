package com.evoucher.adminapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemType {
    INTERNAL,

    EXTERNAL,
    GIFTPOP,
    UR_BOX,
    WATANE,

    CHOICE,
    BULK,
    VNPT_EPAY,
    XPAY
}
