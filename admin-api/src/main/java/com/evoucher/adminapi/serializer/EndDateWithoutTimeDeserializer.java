package com.evoucher.adminapi.serializer;

import com.evoucher.adminapi.common.utils.Constant;
import com.evoucher.adminapi.common.utils.DateUtils;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EndDateWithoutTimeDeserializer extends JsonDeserializer<Date> {
    private static final SimpleDateFormat formatter = new SimpleDateFormat(Constant.Common.COMMON_DATE_FORMAT);


    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String dateStr = jsonParser.getValueAsString();
        if (StringUtils.isBlank(dateStr)) return null;
        try {
            return DateUtils.atEndOfDay(formatter.parse(dateStr));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}