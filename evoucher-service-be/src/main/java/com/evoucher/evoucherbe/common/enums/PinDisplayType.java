package com.evoucher.evoucherbe.common.enums;

public enum PinDisplayType {
    QRCODE,
    BARCODE,
    BARCODE_39,
    TEXT,
    QRBAR;

    public static PinDisplayType fromString(String value) {
        for (PinDisplayType type : PinDisplayType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PinDisplayType: " + value);
    }
}
