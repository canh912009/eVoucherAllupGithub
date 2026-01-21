package asia.castis.evoucher.api.common;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;


@Slf4j
public class DateUtils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final int GMT_7 = 7;
    public static final SimpleDateFormat SIMPLE_DATETIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    // Convert Date to String with default formatting
    public static String toDateTimeString(Date date) {
        return Objects.nonNull(date) ? SIMPLE_DATETIME_FORMATTER.format(date) : null;
    }

    // Get current date as Date object
    public static Date getCurrentDate() {
        return new Date();
    }

    // Get current date as formatted string
    public static String getCurrentDateTimeString() {
        return SIMPLE_DATETIME_FORMATTER.format(new Date());
    }

    public static String getCurrentDateString() {
        return SIMPLE_DATE_FORMATTER.format(new Date());
    }

    // Convert a string to Date
    public static Date toDateTime(String dateString) {
        if (nullStringReturnNull(Objects.isNull(dateString))) return null;
        try {
            return SIMPLE_DATETIME_FORMATTER.parse(dateString);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private static boolean nullStringReturnNull(boolean dateString) {
        if (dateString) {
            log.warn("Input date string is null");
            return true;
        }
        return false;
    }

}
