package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import com.evoucher.adminapi.common.utils.Constant;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VnptCard {
    String provider;
    Integer amount;
    String serial;
    String pin;
    @DateTimeFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    Date expire;
}
