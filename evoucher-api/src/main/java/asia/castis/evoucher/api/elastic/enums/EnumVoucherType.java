package asia.castis.evoucher.api.elastic.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumVoucherType {
    DC("DC"),
    SI("SI"),
    PP("PP");

    private final String value;

    EnumVoucherType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumVoucherType fromValue(String value) {
        for (EnumVoucherType enumPublishStatus : EnumVoucherType.values()) {
            if (enumPublishStatus.value.equalsIgnoreCase(value)) {
                return enumPublishStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for Publish status: " + value);
    }
}
