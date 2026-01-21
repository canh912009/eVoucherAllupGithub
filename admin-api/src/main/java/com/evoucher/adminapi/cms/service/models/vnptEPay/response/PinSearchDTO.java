package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import com.evoucher.adminapi.common.utils.Constant;
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
public class PinSearchDTO {
    Long id;
    String giftTitle;
    String brandName;
    Long price;
    Integer quantity;
    @DateTimeFormat(pattern = Constant.Common.COMMON_DATE_FORMAT)
    Date createDate;
}
