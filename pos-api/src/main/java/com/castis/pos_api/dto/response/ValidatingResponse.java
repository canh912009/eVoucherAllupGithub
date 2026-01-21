package com.castis.pos_api.dto.response;

import com.castis.pos_api.dto.GoodsDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidatingResponse {
    String ev;
    String transactionId;
    String userPhoneNo;
    Date expireDate;
    Long voucherPrice;
    Integer voucherType;
    Long balance;
    GoodsDto goods;
}
