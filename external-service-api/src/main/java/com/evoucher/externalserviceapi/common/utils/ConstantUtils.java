package com.evoucher.externalserviceapi.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class ConstantUtils {

    public static Gson gson = new GsonBuilder().setDateFormat(Common.COMMON_DATETIME_FORMAT).excludeFieldsWithModifiers(Modifier.STATIC).create();

    public static final String HEADER_AUTH_TOKEN = "Authorization";

    public static class Common {
        public static final String COMMON_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String COMMON_DATE_FORMAT = "yyyy-MM-dd";
    }
}
