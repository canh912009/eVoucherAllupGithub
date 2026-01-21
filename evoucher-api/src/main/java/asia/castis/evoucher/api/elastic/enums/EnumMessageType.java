package asia.castis.evoucher.api.elastic.enums;

public enum EnumMessageType {
    ZALO("ZALO"), SMS("SMS"), DOWNLOAD("DOWNLOAD"), PAPER("PAPER");

    private String value;

    EnumMessageType(String value) {
        this.value = value;
    }

    public static EnumMessageType getEnum(String value) {
        if ("ZALO".equalsIgnoreCase(value)) {
            return ZALO;
        } else if ("DOWNLOAD".equalsIgnoreCase(value)){
            return DOWNLOAD;
        } else if ("PAPER".equalsIgnoreCase(value)){
            return PAPER;
        } else {
            return SMS;
        }
    }
}
