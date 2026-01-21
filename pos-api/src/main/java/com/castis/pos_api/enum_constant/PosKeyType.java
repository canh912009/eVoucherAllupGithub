package com.castis.pos_api.enum_constant;

import lombok.Getter;

public enum PosKeyType {
    PIN(1), SERIAL_NO(2);

    @Getter
    private int code;

    PosKeyType(int code) {
        this.code = code;
    }
}
