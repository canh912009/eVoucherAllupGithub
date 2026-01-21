package com.evoucher.adminapi.good.service.typed_service.system.watane;

public enum WataneErrorCode {
    MODEL_INVALID("model.invalid", 6000, "Invalid request data"),
    TRANSACTION_IN_PROGRESS("transaction.inprogress", 6001, "Transaction is in progress. Please call API check transaction status (4.4) later"),
    TRANSACTION_TIMEOUT("transaction.timeout", 6002, "Transaction timeout. Please call API check transaction status (4.4) later"),
    TRANSACTION_FAILED("transaction.failed", 6003, "Transaction failed"),
    PRODUCT_NOT_FOUND("product.notfound", 6004, "Product is not found"),
    PRODUCT_NOT_SUPPORTED("product.notsupported", 6005, "Product is not supported"),
    TRANSACTION_DUPLICATED("transaction.duplicated", 6006, "Transaction already exists"),
    ORDER_PAYMENT_FAILED("order.payment_failed", 6007, "Internal payment failed. Please contact Payoo for support."),
    ORDER_GENERATE_CODES_ERROR("order.generate_codes_error", 6008, "Error in generating voucher. Please contact Payoo for support."),
    COMMON_ERROR("common.error", 6009, "System error. Please contact Payoo for support."),
    COMMON_SYSTEM("common.system", 6010, "System error. Please contact Payoo for support."),
    ERROR_WHILE_CREATING_REQUEST("create.request.error", 6011, "Error while creating Watane request"),
    GENERAL_EXCEPTION("general", 6099, "Error while requesting to Watanes API"),;

    private final String wataneCode;
    private final int aquaCode;
    private final String message;

    WataneErrorCode(String wataneCode, int aquaCode, String message) {
        this.wataneCode = wataneCode;
        this.aquaCode = aquaCode;
        this.message = message;
    }

    public String getWataneCode() {
        return wataneCode;
    }

    public int getAquaCode() {
        return aquaCode;
    }

    public String getMessage() {
        return message;
    }

    public static WataneErrorCode fromWataneCode(String wataneCode) {
        for (WataneErrorCode errorCode : WataneErrorCode.values()) {
            if (errorCode.wataneCode.equals(wataneCode)) {
                return errorCode;
            }
        }
        return GENERAL_EXCEPTION;
    }
}
