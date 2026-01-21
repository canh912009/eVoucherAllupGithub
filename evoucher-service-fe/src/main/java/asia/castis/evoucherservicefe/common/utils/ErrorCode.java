package asia.castis.evoucherservicefe.common.utils;

public class ErrorCode {
    public static final int UNKNOWN_ERROR = 10001;
    public static final int VOUCHER_NOT_FOUND = 1001;
    public static final int EXCHANGE_FAIL = 1002;
    public static final int VOUCHER_USED = 1003;
    public static final int VOUCHER_CANNOT_TRANSFER = 1004;
    public static final int VOUCHER_TRANSFER_PROCESSING = 1005;
    public static final int VOUCHER_LIMIT_AMOUNT = 1006;
    public static final int OTP_INVALID_OR_EXPIRE = 1007;
    public static final int GENERATE_OTP_FAIL = 1008;
    public static final int VOUCHER_REMAINING_SMALL = 1009;

    public static final int VOUCHER_CANNOT_RECEIPT = 1010;
    public static final int VOUCHER_NOT_WAITING_RECEIPT = 1011;
    public static final int CAN_NOT_FIND_HISTORY_TRANSFER = 1012;
    public static final int CAN_NOT_FIND_OLD_VOUCHER = 1013;
    public static final int CANCEL_PAYMENT_EXPIRE = 1014;
    public static final int CANCEL_VOUCHER_PAYMENT_NOT_USE = 1015;
    public static final int CANCEL_VOUCHER_NOT_USE = 1016;
    public static final int PAYMENT_HISTORY_NOT_FOUND = 1017;
    public static final int CANCEL_STORE_NOT_SAME = 1018;
    public static final int CANCEL_AMOUNT_GREATER = 1019;
    public static final int VOUCHER_IS_EXPIRE = 1020;
    public static final int STORE_NOT_FOUND = 1021;

    public static final int VOUCHER_IS_DISABLE = 1022;
    public static final int VOUCHER_IS_TRANSFER = 1023;
    public static final int VOUCHER_DISABLE = 1022;
    public static final int DUPLICATE_TRANSFER_MOBILE_NUMBER = 1024;

    public static final int INVALID_TOKEN = 1025;
    public static final int CAN_NOT_FIND_PUBLISH = 1026;
    public static final int OTP_ALREADY_EXIST = 1027;
    public static final int CAN_NOT_CREATE_CHOICE_VOUCHER = 1028;
    public static final int INVALID_SHORT_LINK = 10010;
    public static final int ACTIVATE_QR_NOT_FOUND = 4004;
    public static final int VOUCHER_NOT_FOUND_BY_SERIAL_NO = 4001;
    public static final int VOUCHER_ALREADY_ACTIVATED = 4005;
    public static final int VOUCHER_INACTIVE = 4006;
    public static final int GENERAL_NOT_FOUND = 1999;
    public static final int INVALID_REQUEST = 6000;
}
