package com.castis.publishservice.utils.enum_template;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumTemplateKey {
    customerName("customerName"),
    sender("sender"),
    productName("productName"),
    expireDate("expireDate"),
    transferMessage("transferMessage"),
    shortLink("shortLink"),
    cta2("cta2"),
    message("message");

    private final String value;

    EnumTemplateKey(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumTemplateKey fromValue(String value) {
        for (EnumTemplateKey key : EnumTemplateKey.values()) {
            if (key.value.equalsIgnoreCase(value)) {
                return key;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
