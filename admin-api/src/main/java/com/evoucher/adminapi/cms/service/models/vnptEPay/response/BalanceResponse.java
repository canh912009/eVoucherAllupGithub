package com.evoucher.adminapi.cms.service.models.vnptEPay.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalanceResponse {
    Long balance;
}
