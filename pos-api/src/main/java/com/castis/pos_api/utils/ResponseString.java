package com.castis.pos_api.utils;

public class ResponseString {
    public static final String UNKNOWN_ERROR = "Unknown error";
    public static final String VOUCHER_NOT_FOUND = "Voucher not found";
    public static final String EXCHANGE_FAIL = "Exchange fail";
    public static final String VOUCHER_USED = "Voucher used";
    public static final String VOUCHER_CANNOT_TRANSFER = "The voucher status not normal or not partial use";
    public static final String VOUCHER_TRANSFER_PROCESSING = "Voucher is being transferred";
    public static final String VOUCHER_LIMIT_AMOUNT = "Not enough money";
    public static final String OTP_INVALID_OR_EXPIRE = "Otp invalid or expire";
    public static final String GENERATE_OTP_FAIL = "Generate otp fail";

    public static final String VOUCHER_REMAINING_SMALL = "The remaining value of the voucher is less than half of the original value. Cannot transfer";
    public static final String VOUCHER_CANNOT_RECEIPT = "The voucher status not normal or not partial use";
    public static final String VOUCHER_NOT_WAITING_RECEIPT = "The voucher status not waiting receipt";
    public static final String CAN_NOT_FIND_HISTORY_TRANSFER = "Can't find transfer history";
    public static final String CAN_NOT_FIND_OLD_VOUCHER = "Can't find old voucher";

    public static final String CANCEL_PAYMENT_EXPIRE = "Payment cancel expire";
    public static final String CANCEL_VOUCHER_PAYMENT_NOT_USE = "The transaction cannot be canceled because the transaction has been cancelled";
    public static final String CANCEL_VOUCHER_NOT_USE = "The voucher can't cancel because voucher status not use";
    public static final String PAYMENT_HISTORY_NOT_FOUND = "Payment not found";
    public static final String CANCEL_STORE_NOT_SAME = "You need to cancel the transaction at the store you exchanged before";
    public static final String CANCEL_AMOUNT_GREATER = "The refund amount is greater than the original amount of the voucher";

    public static final String VOUCHER_IS_EXPIRE = "Voucher is expire";
    public static final String PUBLIC_ID_ERROR = "Generate otp fail";
    public static final String STORE_NOT_FOUND = "Store not found";
}
