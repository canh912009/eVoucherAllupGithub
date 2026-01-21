package asia.castis.evoucher.api.elastic.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumExchangeType {
    USE("USE"),
    CANCEL("CANCEL");
    private final String value;

    EnumExchangeType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumExchangeType fromValue(String value) {
        for (EnumExchangeType enumPublishStatus : EnumExchangeType.values()) {
            if (enumPublishStatus.value.equalsIgnoreCase(value)) {
                return enumPublishStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for voucher status: " + value);
    }

}
