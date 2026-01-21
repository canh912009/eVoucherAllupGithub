package com.castis.publishservice.utils.enum_template;

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
