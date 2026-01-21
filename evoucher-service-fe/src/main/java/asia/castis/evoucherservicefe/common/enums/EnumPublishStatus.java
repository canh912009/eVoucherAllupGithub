package asia.castis.evoucherservicefe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumPublishStatus {
    PUBLISHING("PUBLISHING"),
    FAIL_PUBLISHING("FAIL_PUBLISHING"),
    GENERATING("GENERATING"),
    FAIL_GENERATING("FAIL_GENERATING"),
    SENDING("SENDING"),
    FAIL_SENDING("FAIL_SENDING"),
    WAIT_FOR_SEND_RESULT("WAIT_FOR_SEND_RESULT"),
    FINISHED("FINISHED");

    private final String value;

    EnumPublishStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumPublishStatus fromValue(String value) {
        for (EnumPublishStatus enumPublishStatus : EnumPublishStatus.values()) {
            if (enumPublishStatus.value.equalsIgnoreCase(value)) {
                return enumPublishStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for Publish status: " + value);
    }
}
