package com.castis.pos_api.enum_constant;

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
    VNPT_EPAY
}
