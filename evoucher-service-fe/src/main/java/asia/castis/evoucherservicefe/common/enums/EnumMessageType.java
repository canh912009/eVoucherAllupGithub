package asia.castis.evoucherservicefe.common.enums;

public enum EnumMessageType {
    ZALO("ZALO"), SMS("SMS"), DOWNLOAD("DOWNLOAD"), PAPER("PAPER"), EMAIL("EMAIL");

    private String value;

    EnumMessageType(String value) {
        this.value = value;
    }

    public static EnumMessageType getEnum(String value) {
        if ("ZALO".equalsIgnoreCase(value)) {
            return ZALO;
        } else if ("DOWNLOAD".equalsIgnoreCase(value)) {
            return DOWNLOAD;
        } else if ("PAPER".equalsIgnoreCase(value)) {
            return PAPER;
        } else if ("SMS".equalsIgnoreCase(value)) {
            return SMS;
        } else {
            return EMAIL;
        }
    }
    public String getValue() {
        return this.value;
    }
}
