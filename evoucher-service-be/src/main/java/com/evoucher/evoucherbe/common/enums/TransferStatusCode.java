package com.evoucher.evoucherbe.common.enums;

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
