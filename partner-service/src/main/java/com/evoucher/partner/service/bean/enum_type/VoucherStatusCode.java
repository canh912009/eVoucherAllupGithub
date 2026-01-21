package com.evoucher.partner.service.bean.enum_type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoucherStatusCode {
    NORMAL,
    EXPIRE,
    PART_USED,
    USED,
    DISABLED
}
