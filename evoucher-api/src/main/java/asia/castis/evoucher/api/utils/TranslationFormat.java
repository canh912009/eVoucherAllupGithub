package asia.castis.evoucher.api.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TranslationFormat {
    TEXT("text"),
    HTML("html");

    private final String value;

    TranslationFormat(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}