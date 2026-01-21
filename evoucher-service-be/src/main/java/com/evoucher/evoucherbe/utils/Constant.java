package com.evoucher.evoucherbe.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;

public class Constant {

    public static Gson gson = new GsonBuilder().setDateFormat(Common.COMMON_DATETIME_FORMAT).excludeFieldsWithModifiers(Modifier.STATIC).create();
    public static SimpleDateFormat COMMON_DATE_FORMATTER = new SimpleDateFormat(Constant.Common.COMMON_DATETIME_FORMAT);
    public final static String ERROR_CODE = "-1";
    public final static String ANONYMOUS_USER = "ANONYMOUS_USER";
    public final static String SYSTEM = "SYSTEM";
    static final String algorithm = "AES/ECB/PKCS5Padding";
    public final static String VNPT_ERROR_CODE = "-2";

    public static class Common {
        public static final String COMMON_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd";
        public static final String XPAY_DATE_FORMAT = "yyyyMMdd";
        public static final short VERSION_1 = 1;
        public static final short VERSION_2 = 2;
    }
}
