package com.evoucher.adminapi.admin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum SMSType {
    SMS,
    ZALO,
    DOWNLOAD,
    PAPER,
    EMAIL;

    public static boolean isVoucherCountType(final SMSType smsType) {
        return EnumSet.of(DOWNLOAD, PAPER).contains(smsType);
    }

}
