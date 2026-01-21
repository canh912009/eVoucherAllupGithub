package asia.castis.web_hook.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;

public class Common {
    private Common() {}
    public static final String DATETIME_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd ");
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final int SUCCESS = 0;

    public static String toJsonBody(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            return object.toString();
        }
    }
}
