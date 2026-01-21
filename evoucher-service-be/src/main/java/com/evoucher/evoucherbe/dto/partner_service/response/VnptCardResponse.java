package com.evoucher.evoucherbe.dto.partner_service.response;

import com.evoucher.evoucherbe.utils.Constant;
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
public class VnptCardResponse {
    String provider;
    Integer amount;
    String serial;
    String pin;
    @JsonFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    @DateTimeFormat(pattern = Constant.Common.COMMON_DATETIME_FORMAT)
    Date expire;
}
