package com.castis.pos_api.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class Constants {
    static final String algorithm = "AES/ECB/PKCS5Padding";
    public static final String SUCCESS = "OK";

    public static final String CONTENT_TYPE = "Content-Type";

    public static final String APPLICATION_JSON = "application/json";

    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_DATE = "yyyyMMdd";
    public static final String FORMAT_DATE_HYPHEN = "YYYY-MM-DD";

    public static final String FORMAT_TIME = "HHmmss";


    public static final String WEB_POS_AUTH_SOURCE = "/webpos/vouchers/otp/";
    public static final String WEB_POS_CONFIRM_SOURCE = "/webpos/vouchers/exchange";
    public static final String WEB_POS_CANCEL_SOURCE = "/webpos/vouchers/cancelPayment";

    public static final int WEB_POS = 1;

    public static final int POS = 2;

}
