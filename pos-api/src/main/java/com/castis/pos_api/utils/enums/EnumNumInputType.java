package com.castis.pos_api.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumNumInputType {
    SCAN(0),
    MANUAL(1);

    private int value;
}
