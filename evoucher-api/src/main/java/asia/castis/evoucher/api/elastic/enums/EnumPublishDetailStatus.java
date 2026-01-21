package asia.castis.evoucher.api.elastic.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumPublishDetailStatus {
    STRT_PUB("STRT_PUB"),
    FAIL_PUB("FAIL_PUB"),
    END_PUB("END_PUB"),
    STRT_GEN_MSG("STRT_GEN_MSG"),
    FAIL_GEN_MSG("FAIL_GEN_MSG"),
    END_GEN_MSG("END_GEN_MSG"),
    STRT_SND_MSG("STRT_SND_MSG"),
    FAIL_SND_MSG("FAIL_SND_MSG"),
    RESULT_PENDING("RESULT_PENDING"),
    RESULT_SUCCESS("RESULT_SUCCESS"),
    RESULT_FAIL("RESULT_FAIL");

    private final String value;

    EnumPublishDetailStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumPublishDetailStatus fromValue(String value) {
        for (EnumPublishDetailStatus enumPublishDetailStatus : EnumPublishDetailStatus.values()) {
            if (enumPublishDetailStatus.value.equalsIgnoreCase(value)) {
                return enumPublishDetailStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for Publish detail status: " + value);
    }
}
