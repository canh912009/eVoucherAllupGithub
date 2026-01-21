package com.evoucher.partner.service.vnpt.bean.response;

import com.evoucher.partner.service.common.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    String provider;
    Integer amount;
    String serial;
    String pin;
    @JsonFormat(pattern = DateUtils.COMMON_DATE_FORMAT)
    @DateTimeFormat(pattern = DateUtils.COMMON_DATE_FORMAT)
    Date expire;
}

