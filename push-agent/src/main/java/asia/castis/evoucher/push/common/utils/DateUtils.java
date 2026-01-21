package asia.castis.evoucher.push.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String toString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return LocalDateTime.now().format(formatter);
        }
        return dateTime.format(formatter);
    }
    /*
    * get string date with formart yyy-MM-dd HH:mm:ss
    */
    public static String getStrDate(Date date) {
        return dateFormat.format(date);
    }
    public static LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }

    public static String getCurrentDateString() {
        return LocalDateTime.now().format(formatter);
    }

    public static LocalDateTime toDateTime(String dateString) {
        if (dateString == null) {
            return null;
        }
        return LocalDateTime.parse(dateString, formatter);
    }
}
