package com.evoucher.externalserviceapi.common.utils;

import com.evoucher.externalserviceapi.common.exception.CustomCodeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Slf4j
public class DateUtils {
    public static final SimpleDateFormat formatter = new SimpleDateFormat(
            ConstantUtils.Common.COMMON_DATETIME_FORMAT);

    public static Date generateLocalDateToStartDay(LocalDate localDate) {
        if (Objects.isNull(localDate)) return null;

        LocalDateTime localDateTime = localDate.atStartOfDay();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date generateLocalDateToEndDay(LocalDate localDate) {
        if (Objects.isNull(localDate)) return null;

        LocalDateTime localDateTime = localDate.atTime(LocalTime.of(23, 59, 59));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String generateLocalDateToString(LocalDate localDate, String pattern) {
        if (Objects.isNull(localDate) || StringUtils.isBlank(pattern)) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    public static Date getDateFromStringWithCommonFormat(String date) {
        try {
            if (date == null || date.isBlank()) {
                return null;
            }
            return formatter.parse(date);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
            throw new CustomCodeException(
                    MessageUtils.getMessage("evoucher.error.can_not_parse_date", date),
                    HttpStatus.BAD_REQUEST);
        }
    }

    public static Date atStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static Date atEndOfDay(Date date) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
}
