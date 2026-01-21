package asia.castis.evoucherservicefe.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonMapper {

    private static ObjectMapper objectMapper;

    private JsonMapper() {
    }

    public static synchronized ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return objectMapper;
    }

    /***
     * Write value as JSON, if can not fallback to normal write value as String
     * This should only be used in read-only cases
     * @param input
     * @return
     */
    public static String safeWriteValueAsString(Object input) {
        try {
            return getInstance().writeValueAsString(input);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            return input.toString();
        }
    }

}
