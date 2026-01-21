package asia.castis.evoucherservicefe.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalDateUtils {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static Pattern dateTimePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
    private static Pattern datePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    public static String toString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return LocalDateTime.now().format(dateTimeFormatter);
        }
        return dateTime.format(dateTimeFormatter);
    }

    public static LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }

    public static String getCurrentDateString() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }

    public static LocalDateTime toDateTime(String dateTimeString) {
        if (dateTimeString == null) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    public static LocalDateTime toDateTime(String dateStringWithoutTime, boolean defaultEndDate) {
        if (dateStringWithoutTime == null) {
            return null;
        }
        Matcher matcher = dateTimePattern.matcher(dateStringWithoutTime);
        if (matcher.matches()) {
            return LocalDateTime.parse(dateStringWithoutTime, dateTimeFormatter);
        }

        // Date case
        LocalDateTime localDateTime = LocalDate.parse(dateStringWithoutTime, dateFormatter).atStartOfDay();
        LocalDateTime finalDateTime;
        if (defaultEndDate) {
            finalDateTime = localDateTime.withHour(23).withMinute(59).withSecond(59);
        } else {
            finalDateTime = localDateTime;
        }
        return finalDateTime;
    }
}
