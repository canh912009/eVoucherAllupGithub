package com.castis.pos_api.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public enum EnumPosType {
    WEB_POS(1),
    POS(2);

    private int value;
}
