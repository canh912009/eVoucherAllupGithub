package com.castis.pos_api.dto.response;

import com.castis.pos_api.utils.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherResponse {
    String ev;
    String transactionId;
    String userPhoneNo;
    @JsonFormat(pattern = Constants.FORMAT_DATE_HYPHEN)
    Date expireDate;
    Double voucherPrice;
    Integer voucherType;
    Double balance;
    GoodResponse goods;
}
