package asia.castis.evoucher.api.elastic.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumVoucherStatus {
    NORMAL("NORMAL"),
    EXPIRE("EXPIRE"),
    PART_USED("PART_USED"),
    USED("USED"),
    DISABLED("DISABLED");
    private final String value;

    EnumVoucherStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumVoucherStatus fromValue(String value) {
        for (EnumVoucherStatus enumPublishStatus : EnumVoucherStatus.values()) {
            if (enumPublishStatus.value.equalsIgnoreCase(value)) {
                return enumPublishStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for voucher status: " + value);
    }

}
