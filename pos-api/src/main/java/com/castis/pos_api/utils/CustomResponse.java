package com.castis.pos_api.utils;


import lombok.Getter;

@Getter
public enum CustomResponse {
    SUCCESS(0, "Success"),
    E4001_APP_ID(4001, "AppId not found or customer with given appId is invalid"),
    E4002_AUTH_CD(4002, "Authentication code is invalid"),
    E4004_IP_NOT_ALLOWED(4004, "IP address not allowed for this action"),
    E4101_INVALID_REQUEST(4101, "Invalid request. Missing mandatory field"),
    E4404_NOT_FOUND(4404, "Not found exception"),
    E4401_INVALID_VOUCHER_STATE(4401, "Invalid voucher state"),
    E4421_INVALID_BRAND_STATE(4421, "Invalid brand state"),
    E4431_INVALID_STORE_STATE(4431, "Invalid store state"),
    E4441_INVALID_GOODS_STATE(4441, "Invalid product state"),
    E5001_INTERNAL_SERVER_ERROR(5001, "Internal server error. Check the detailed message"),
    E6001_VOUCHER_IS_PROCESSING(6001, "Voucher is being used by another process"),
    E6002_ENCRYPTION_ERROR(6002, "Exception while encrypting/ decrypting data"),
    E6003_NOT_ENOUGH_BALANCE(6003, "Not enough balance");


    private final int code;
    private final String message;

    CustomResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code + ": " + message;
    }
}
