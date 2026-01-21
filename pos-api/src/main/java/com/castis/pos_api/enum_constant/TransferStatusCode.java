package com.castis.pos_api.enum_constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransferStatusCode {
    TRANSFER,
    RECPT_WAIT,
    RECPTED,
    RETURN
}
