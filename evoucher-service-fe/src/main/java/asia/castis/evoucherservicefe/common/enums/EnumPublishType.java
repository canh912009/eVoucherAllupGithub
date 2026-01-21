package asia.castis.evoucherservicefe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumPublishType {
    NORMAL("NORMAL"),
    RESEND("RESEND"),
    TRANSFER("TRANSFER");
    private final String value;

    EnumPublishType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumPublishType fromValue(String value) {
        for (EnumPublishType enumPublishType : EnumPublishType.values()) {
            if (enumPublishType.value.equalsIgnoreCase(value)) {
                return enumPublishType;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}

