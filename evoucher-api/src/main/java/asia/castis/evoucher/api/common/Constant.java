package asia.castis.evoucher.api.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Constant
 *
 * @author by castis on 3/26/2023
 */

public class Constant {
    public static final String OTP_SERVICE_BASE_URL = "http://192.168.0.125:19931/";

    public static final String SUCCESS = "OK";
    public static final int BE_SUCCESS_CODE = 0;
    public static final int VERSION_1 = 1;
    public static final int VERSION_2 = 2;


    private Constant() {
        throw new IllegalStateException("Constant class");
    }

    public static final Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

    public static class FormatDate {

        private FormatDate() {
            throw new IllegalStateException("FormatDate class");
        }

        public static final String DATE_TIME_DEFAULT = "yyyy-MM-dd HH:mm:ss";

        public static final String DATE_DEFAULT = "yyyy-MM-dd";

    }

    public static class Pagination {

        private Pagination() {
            throw new IllegalStateException("Pagination class");
        }

        public static final String DEFAULT_PAGE = "1";
        public static final String DEFAULT_SIZE = "10";

    }

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateTimeAdapter.DateAdapter())
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

}
