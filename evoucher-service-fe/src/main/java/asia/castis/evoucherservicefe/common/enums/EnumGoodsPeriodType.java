package asia.castis.evoucherservicefe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumGoodsPeriodType {
    FIXED_TERM("FIXED_TERM"), FIXED_DT("FIXED_DT");
    private final String value;

    EnumGoodsPeriodType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumGoodsPeriodType fromValue(String value) {
        for (EnumGoodsPeriodType enumVal : EnumGoodsPeriodType.values()) {
            if (enumVal.value.equalsIgnoreCase(value)) {
                return enumVal;
            }
        }
        throw new IllegalArgumentException("Invalid value for period Type: " + value);
    }
}
