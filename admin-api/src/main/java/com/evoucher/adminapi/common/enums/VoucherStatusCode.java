package com.evoucher.adminapi.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum VoucherStatusCode {
    NORMAL,
    EXPIRE,
    PART_USED,
    USED,
    DISABLED;

    public static final EnumSet<VoucherStatusCode> CAN_NOT_BE_DISABLE_VOUCHER = EnumSet.of(DISABLED, USED, EXPIRE);
}
