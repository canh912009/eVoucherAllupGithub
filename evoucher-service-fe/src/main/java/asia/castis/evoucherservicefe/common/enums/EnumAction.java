package asia.castis.evoucherservicefe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumAction {
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String value;

    EnumAction(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumAction fromValue(String value) {
        for (EnumAction status : EnumAction.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
