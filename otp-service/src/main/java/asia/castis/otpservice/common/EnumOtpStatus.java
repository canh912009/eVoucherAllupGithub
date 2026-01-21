package asia.castis.otpservice.common;

public enum EnumOtpStatus {
    NORMAL((byte) 0), USED((byte) 1), UNKNOWN((byte) 9);

    private byte code;

    EnumOtpStatus(byte code) {
        this.code = code;
    }

    public static EnumOtpStatus get(byte code) {
        if (code == (byte) 0) {
            return NORMAL;
        }
        if (code == (byte) 1) {
            return USED;
        }
        return UNKNOWN;
    }

    public byte getCode() {
        return this.code;
    }
}
