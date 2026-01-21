package com.castis.publishservice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;

public class Constants {
    public static final int SUCCESS_CODE = 0;
    public static final String fullDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static class JOB_CONSTANTS {
        public static final String JOB_NAME_PREFIX = "PUBLISH_";
        public static final String JOB_GROUP_PREFIX = "CAMPAIGN_";
        public static final String TRIGGER_NAME_PREFIX = "PUBLISH_";
        public static final String TRIGGER_GROUP_PREFIX = "CAMPAIGN_";

    }

    public static final Gson gson = new GsonBuilder()
            .setDateFormat(fullDateTimeFormat)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    public static class ERROR_CODE {
        public static final int INTERNAL_ERROR_CODE = 10000;
        public static final int VOUCHER_NOT_FOUND = 1001;
        public static final int INACTIVE_BRAND = 10011;
        public static final int INACTIVE_GOOD = 10012;
        public static final int CHOICE_GOOD_ID_NULL = 10013;
    }
}
