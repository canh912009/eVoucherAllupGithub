package com.castis.publishservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class DateUtils {
    public static final DateTimeFormatter slashFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter wataneFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static Date generateStringDateToDateEndDay(String dateString, DateTimeFormatter formatter) {
        if (StringUtils.isBlank(dateString)) return null;

        LocalDate localDate = LocalDate.parse(dateString, formatter);

        LocalDateTime localDateTime = localDate.atTime(LocalTime.of(23, 59, 59));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static Date epochTimeToDate(Long epochTime) {
        return new Date(epochTime * 1000);
    }

    public static Date getDateFromSlashFormat(String string) {
        if (StringUtils.isBlank(string)) return null;

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(string, slashFormatter);
        } catch (Exception e) {
            log.error(String.format("Exception parsing date: %s. Set default to unlimited", string));
            return Date.from(LocalDateTime.of(9999, 12, 31, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        }

        LocalDateTime localDateTime = localDate.atTime(LocalTime.of(0, 0, 0));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getDateFromWataneFormat(String string) {
        if (StringUtils.isBlank(string)) return null;

        LocalDate localDate;
        try {
            localDate = LocalDate.parse(string, wataneFormatter);
        } catch (Exception e) {
            log.error(String.format("Exception parsing date: %s. Set default to unlimited", string));
            return Date.from(LocalDateTime.of(9999, 12, 31, 0, 0, 0).atZone(ZoneId.systemDefault()).toInstant());
        }

        LocalDateTime localDateTime = localDate.atTime(LocalTime.of(0, 0, 0));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
