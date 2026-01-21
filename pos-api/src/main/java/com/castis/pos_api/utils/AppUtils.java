package com.castis.pos_api.utils;

import com.castis.pos_api.utils.enums.EnumNumInputType;
import com.castis.pos_api.utils.enums.EnumPosFuncType;

public class AppUtils {

    public static boolean IS_AUTH_VOUCHER(String func) {
        return EnumPosFuncType.AUTH.getValue().equals(func);
    }

    public static boolean IS_CONFIRM_VOUCHER(String func) {
        return EnumPosFuncType.CONFIRM.getValue().equals(func);
    }

    public static boolean IS_CANCEL_VOUCHER(String func) {
        return EnumPosFuncType.CANCEL.getValue().equals(func);
    }

    public static boolean IS_AUTH_OR_CONFIRM_VOUCHER(String func) {
        return IS_AUTH_VOUCHER(func) || IS_CONFIRM_VOUCHER(func);
    }

    public static boolean IS_CANCEL_OR_CONFIRM_VOUCHER(String func) {
        return IS_CANCEL_VOUCHER(func) || IS_CONFIRM_VOUCHER(func);
    }

    public static boolean IS_NOT_FUNC_VOUCHER(String func) {
        return !(IS_AUTH_VOUCHER(func) || IS_CONFIRM_VOUCHER(func) || IS_CANCEL_VOUCHER(func));
    }

    public static boolean IS_SCAN_INPUT(int type) {
        return EnumNumInputType.SCAN.getValue() == type;
    }

    public static boolean IS_MANUAL_INPUT(int type) {
        return EnumNumInputType.MANUAL.getValue() == type;
    }

    public static boolean IS_INVALID_INPUT(int type) {
        return !(IS_SCAN_INPUT(type) || IS_MANUAL_INPUT(type));
    }
}
