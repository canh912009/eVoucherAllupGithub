package asia.castis.evoucher.api.utils;

public class ErrorCode {

    public static final int VOUCHER_NOT_FOUND = 1001;
    public static final int EXCHANGE_FAIL = 1002;
    public static final int VOUCHER_USED = 1003;
    public static final int INVALID_VOUCHER_STATUS = 1004;
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
    public static final int VOUCHER_DISABLE = 1022;
    public static final int VOUCHER_IS_TRANSFER = 1023;
    public static final int DUPLICATE_TRANSFER_MOBILE_NUMBER = 1024;
    public static final int INVALID_TOKEN = 1025;
    public static final int PRODUCT_QUANTITY_NOT_ENOUGH = 1026;
    public static final int OTP_ALREADY_EXIST = 1027;
    public static final int CAN_NOT_TRANSFER_CHOICE_VOUCHER = 1027;
    public static final int CAN_NOT_CREATE_CHOICE_VOUCHER = 1028;
    public static final int CAN_NOT_ACTIVATE_VOUCHER = 1029;

    public static final int CAN_NOT_FIND_PUBLISH = 1088;
    public static final int CAN_NOT_FIND_USER = 1089;
    public static final int CAN_NOT_FIND_CATEGORY = 1090;
    public static final int CAN_NOT_FIND_BRAND = 1091;
    public static final int CAN_NOT_FIND_GOODS = 1092;
    public static final int CAN_NOT_FIND_SUPPLIER = 1093;

    public static final int GENERAL_NOT_FOUND = 1999;

    public static final int VOUCHER_NOT_FOUND_BY_SERIAL_NO = 4001;
    public static final int ACTIVATE_QR_URL_NOT_FOUND = 4004;
    public static final int VOUCHER_ALREADY_ACTIVATED = 4005;
    public static final int VOUCHER_INACTIVE = 4006;
    public static final int ACTIVATION_KEY_MISMATCH = 4040;
    public static final int SERIAL_NO_MISMATCH = 4041;

    public static final int PUBLISH_NOT_FOUND = 5001;
    public static final int INVALID_REQUEST = 6000;

    public static final int VOUCHER_IS_BEING_PROCESSED = 8888;
    public static final int EXCEPTION_WHILE_REQUESTING_FE = 9898;

    public static final int UNKNOWN_ERROR = 10001;
    public static final int INVALID_SHORT_LINK = 10010;
    public static final int INACTIVE_BRAND = 10011;
    public static final int INACTIVE_GOOD = 10012;
    public static final int CHOICE_GOOD_ID_NULL = 10013;

    public static final int RECEIVER_PHONE_NO_REQUIRED_FOR_TOPUP = 1201;
    public static final int CAN_NOT_PURCHASE_FROM_VNPT_EPAY = 1209;
    public static final int EXCEPTION_WHILE_REQUESTING_TO_BE = 1210;

    public static final int INVALID_STORE = 1300;

    public static final int OTP_IS_REQUIRED = 1400;
    public static final int OTP_MISMATCH = 1401;

    public static final int VNPT_IS_STILL_IN_PROGRESS = 1500;
}
