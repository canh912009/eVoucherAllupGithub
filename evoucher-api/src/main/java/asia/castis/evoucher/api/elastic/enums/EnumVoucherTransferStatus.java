package asia.castis.evoucher.api.elastic.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EnumVoucherTransferStatus {
    DISABLE("DISABLE"),
    TRANSFER("TRANSFER"),
    RECPT_WAIT("RECPT_WAIT"),
    RECPTED("RECPTED"),
    RETURN("RETURN");
    private final String value;

    EnumVoucherTransferStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EnumVoucherTransferStatus fromValue(String value) {
        for (EnumVoucherTransferStatus enumPublishStatus : EnumVoucherTransferStatus.values()) {
            if (enumPublishStatus.value.equalsIgnoreCase(value)) {
                return enumPublishStatus;
            }
        }
        throw new IllegalArgumentException("Invalid value for voucher status: " + value);
    }

}
