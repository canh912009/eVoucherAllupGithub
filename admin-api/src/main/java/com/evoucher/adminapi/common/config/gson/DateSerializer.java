package com.evoucher.adminapi.common.config.gson;

import com.evoucher.adminapi.common.utils.Constant;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSerializer implements JsonSerializer<Date> {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.Common.COMMON_DATETIME_FORMAT);

    @Override
    public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(dateFormat.format(date));
    }
}
