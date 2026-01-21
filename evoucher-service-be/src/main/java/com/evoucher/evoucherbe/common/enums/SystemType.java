package com.evoucher.evoucherbe.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public enum SystemType {
    INTERNAL,
    EXTERNAL,
    CHOICE,
    GIFTPOP,
    UR_BOX,
    BULK,
    VNPT_EPAY,
    WATANE,
    XPAY
    ;

    public static final Set<SystemType> SYSTEM_TYPE_HAS_EXTERNAL_PIN = EnumSet.of(EXTERNAL, GIFTPOP, UR_BOX, WATANE);
}
