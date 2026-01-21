package asia.castis.evoucher.api.utils;

public class ResponseString {
    public static final String UNKNOWN_ERROR = "Unknown error";
    public static final String VOUCHER_NOT_FOUND = "Voucher not found";
    public static final String PUBLISH_NOT_FOUND = "Publish not found";
    public static final String EXCHANGE_FAIL = "Exchange fail";
    public static final String VOUCHER_USED = "Voucher used";
    public static final String VOUCHER_IS_DISABLE = "Voucher is disable";
    public static final String VOUCHER_ALREADY_ACTIVATED = "Voucher is already activated";
    public static final String INVALID_VOUCHER_STATUS = "Invalid voucher status";
    public static final String VOUCHER_IS_BEING_PROCESSED = "Voucher is being processed, please try again after a few second";
    public static final String VOUCHER_TRANSFER_PROCESSING = "Voucher is being transferred";
    public static final String VOUCHER_LIMIT_AMOUNT = "Not enough money";
    public static final String OTP_INVALID_OR_EXPIRE = "Otp invalid or expire";
    public static final String OTP_IS_REQUIRED = "OTP is required";
    public static final String OTP_MISMATCH = "OTP mismatch";
    public static final String GENERATE_OTP_FAIL = "Generate otp fail";

    public static final String VOUCHER_REMAINING_SMALL = "The remaining value of the voucher is less than half of the original value. Cannot transfer";
    public static final String VOUCHER_CANNOT_RECEIPT = "Can not receipt the voucher";
    public static final String VOUCHER_NOT_WAITING_RECEIPT = "The voucher status not waiting receipt";
    public static final String CAN_NOT_FIND_HISTORY_TRANSFER = "Can't find transfer history";
    public static final String CAN_NOT_FIND_OLD_VOUCHER = "Can't find old voucher";
    public static final String CAN_NOT_FIND_PUBLISH = "Can't find publish";
    public static final String CAN_NOT_FIND_USER = "Can't find user";
    public static final String CAN_NOT_FIND_CATEGORY = "Can't find category";
    public static final String CAN_NOT_FIND_BRAND = "Can't find brand";
    public static final String CAN_NOT_FIND_GOODS = "Can't find goods";
    public static final String CAN_NOT_FIND_SUPPLIER = "Can't find supplier";

    public static final String CANCEL_PAYMENT_EXPIRE = "Payment cancel expire";
    public static final String CANCEL_VOUCHER_PAYMENT_NOT_USE = "The transaction cannot be canceled because the transaction has been cancelled";
    public static final String CANCEL_VOUCHER_NOT_USE = "The voucher can't cancel because voucher status not use";
    public static final String PAYMENT_HISTORY_NOT_FOUND = "Payment not found";
    public static final String CANCEL_STORE_NOT_SAME = "You need to cancel the transaction at the store you exchanged before";
    public static final String CANCEL_AMOUNT_GREATER = "The refund amount is greater than the original amount of the voucher";

    public static final String VOUCHER_IS_EXPIRE = "Voucher is expire";
    public static final String PUBLIC_ID_ERROR = "Generate otp fail";
    public static final String STORE_NOT_FOUND = "Store not found";
    public static final String INVALID_STORE = "Store is invalid or store does not have permission to accept the request";
    public static final String STORE_ID_BLANK = "Store Id is blank";
    public static final String DUPLICATE_TRANSFER_MOBILE_NUMBER = "Self transfer is not allowed";

    public static final String INVALID_SHORT_LINK = "invalid short link";
    public static final String SERIAL_NO_MISMATCH = "The provided serial number does not match the voucher info";
    public static final String INVALID_CHOICE_TOKEN = "Invalid token";
    public static final String CAN_NOT_TRANSFER_CHOICE_VOUCHER = "Choice voucher can not be transferred";

    public static final String OTP_ALREADY_EXIST = "OTP already exist";
    public static final String EXCEPTION_WHILE_REQUESTING_TO_BE = "Exception while requesting to BE";

    public static final String VNPT_IS_STILL_IN_PROGRESS = "VNPT is still in progress";
}
