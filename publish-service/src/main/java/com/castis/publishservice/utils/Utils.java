package com.castis.publishservice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PUBLIC)
@Slf4j
public class Utils {
    static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.fullDateTimeFormat);
    public static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    public static String formatFullDateTime(Date date) {
        if (date == null) {
            return null;
        } else {
            return simpleDateFormat.format(date);
        }
    }

    public static boolean mappingYesNo(String value) {
        return "Y".equalsIgnoreCase(value);
    }

    public static String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            log.warn("Error convert object to JSON. Return default toString(): {}", e.getMessage());
            return object.toString();
        }
    }

}
