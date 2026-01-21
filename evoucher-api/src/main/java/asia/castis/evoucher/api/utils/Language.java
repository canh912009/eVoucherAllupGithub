package asia.castis.evoucher.api.utils;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Language {
    VIETNAMESE("vi"),
    ENGLISH("en"),
    KOREAN("ko"),
    JAPANESE("ja"),
    CHINESE("zh");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
