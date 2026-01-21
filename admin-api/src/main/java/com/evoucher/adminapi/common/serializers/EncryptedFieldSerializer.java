package com.evoucher.adminapi.common.serializers;

import com.evoucher.adminapi.common.config.PropertyConverter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class EncryptedFieldSerializer
        extends JsonSerializer<String> {

    private final PropertyConverter converter;

    public EncryptedFieldSerializer(@Autowired PropertyConverter propertyConverter) {
        this.converter = propertyConverter;
    }

    @Override
    public void serialize(
            String value,
            JsonGenerator gen,
            SerializerProvider arg2)
            throws IOException {
        String result = "";
        if (value != null && !value.isBlank()) {
            result = converter.convertToEntityAttribute(value);
        }
        gen.writeString(result);
    }
}
