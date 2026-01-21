package com.castis.publishservice.utils.enum_template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumTemplateType {
    FIXED("FIXED"),
    CUSTOM("CUSTOM"),
    DISABLED("DISABLED");

    private final String value;

    EnumTemplateType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumTemplateType fromValue(String value) {
        for (EnumTemplateType key : EnumTemplateType.values()) {
            if (key.value.equalsIgnoreCase(value)) {
                return key;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
