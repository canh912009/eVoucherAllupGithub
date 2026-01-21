package asia.castis.evoucher.api.elastic.enums;

import java.util.HashMap;
import java.util.Map;

public enum EnumOtpPosType {
    QRCODE(0),
    MANUAL(1);

    private final int value;
    private static final Map<Object, Object> map = new HashMap<>();

    private EnumOtpPosType(int value) {
        this.value = value;
    }

    static {
        for (EnumOtpPosType pageType : EnumOtpPosType.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static EnumOtpPosType valueOf(int pageType) {
        return (EnumOtpPosType) map.get(pageType);
    }

    public int getValue() {
        return value;
    }

}
