package com.castis.pos_api.utils;

import java.util.UUID;

public class DataUtils {
    private DataUtils(){}
    public static String genNewUUID() {
        return UUID.randomUUID().toString();
    }
}
