package com.castis.pos_api.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumPosFuncType {
    AUTH("01"),
    CONFIRM("02"),
    CANCEL("03");

    private String value;

}
