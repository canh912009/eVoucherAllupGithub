package com.evoucher.adminapi.common.config.gson;

import com.evoucher.adminapi.common.utils.Constant;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {

    @Override
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constant.Common.COMMON_DATE_FORMAT);
        return new JsonPrimitive(date.format(dateTimeFormatter));
    }
}
