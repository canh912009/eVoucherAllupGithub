package com.evoucher.evoucherbe.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Modifier;

@Slf4j
public class Utils {
    public static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    public static String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            log.warn("Error convert object to JSON. Return default toString(): {}", e.getMessage());
            return object.toString();
        }
    }

}
