package com.castis.pos_api.enum_constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoucherTypeCode {
    // origin was used to return for pos machine
    SI,
    PP,
    DC,
    CH,
    BK
}
