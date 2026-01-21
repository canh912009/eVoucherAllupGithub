package asia.castis.evoucherservicefe.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.util.Objects;

public class Const {
    public static final String VALID_N = "N";
    public static final String VALID_Y = "Y";
    public static final String COMMON_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static Gson gson = new GsonBuilder()
            .setDateFormat(COMMON_DATETIME_FORMAT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();
    public static String writeToJson(Object obj) {
        return gson.toJson(obj);
    }
}
