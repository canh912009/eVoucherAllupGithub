package asia.castis.evoucherservicefe.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/***
 * TRANSFER/RECPT_WAIT/RECPTED/RETURN
 */
public enum EnumTransferStatus {
    TRANSFER("TRANSFER"),
    RECPT_WAIT("RECPT_WAIT"),
    RECPTED("RECPTED"),
    RETURN("RETURN");
    private final String value;

    EnumTransferStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumTransferStatus fromValue(String value) {
        for (EnumTransferStatus enumPublishStatus : EnumTransferStatus.values()) {
            if (enumPublishStatus.value.equalsIgnoreCase(value)) {
                return enumPublishStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for voucher transfer status: " + value);
    }

}
