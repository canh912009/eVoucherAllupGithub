package asia.castis.evoucherservicefe.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DateUtils {
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
    // Convert a string to Date
    public static Date toDateTime(String dateString) {
        if (nullStringReturnNull(Objects.isNull(dateString))) return null;
        try {
            // Use regex check if it is SIMPLE_DATETIME_FORMATTER format or SIMIPLE_DATE_FORMATTER format
            Pattern dateTimePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
            Matcher matcher = dateTimePattern.matcher(dateString);
            if (matcher.matches()) {
                return SIMPLE_DATETIME_FORMATTER.parse(dateString);
            }
            return SIMPLE_DATE_FORMATTER.parse(dateString);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    // Get current date as formatted string
    public static String getCurrentDateTimeString() {
        return SIMPLE_DATETIME_FORMATTER.format(new Date());
    }

    public static String getCurrentDateString() {
        return SIMPLE_DATE_FORMATTER.format(new Date());
    }

    private static boolean nullStringReturnNull(boolean dateString) {
        if (dateString) {
            log.warn("Input date string is null");
            return true;
        }
        return false;
    }

}
