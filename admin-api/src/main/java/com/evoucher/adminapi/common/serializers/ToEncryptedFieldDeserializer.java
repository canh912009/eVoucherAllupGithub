package com.evoucher.adminapi.common.serializers;

import com.evoucher.adminapi.common.config.PropertyConverter;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class ToEncryptedFieldDeserializer extends JsonDeserializer<String> {
    private final PropertyConverter converter;

    public ToEncryptedFieldDeserializer(@Autowired PropertyConverter propertyConverter) {
        this.converter = propertyConverter;
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String result;
        String originValue = jsonParser.getValueAsString();
        if (originValue != null && !originValue.isBlank()) {
            // encrypt value
            result = converter.convertToDatabaseColumn(originValue);
        } else {
            result = originValue;
        }
        return result;
    }
}
