package asia.castis.evoucherservicefe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumVoucherType {
    SI("SI"),
    DC("DC"),
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
        for (EnumVoucherType status : EnumVoucherType.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
