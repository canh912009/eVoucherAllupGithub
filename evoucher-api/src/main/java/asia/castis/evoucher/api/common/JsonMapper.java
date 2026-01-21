package asia.castis.evoucher.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JsonMapper {

    private static ObjectMapper objectMapper;

    private JsonMapper() {
    }

    public static synchronized ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.registerModule(new JodaModule());
        }
        return objectMapper;
    }

}
