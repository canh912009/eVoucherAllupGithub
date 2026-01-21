package com.castis.publishservice.utils.enum_template;

public enum VoucherDisplayType {
    QRCODE,
    BARCODE,
    BARCODE_39,
    TEXT,
    QRBAR,
    DEFAULT;

    public static VoucherDisplayType fromString(String value) {
        for (VoucherDisplayType type : VoucherDisplayType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Voucher display type: " + value);
    }
}
