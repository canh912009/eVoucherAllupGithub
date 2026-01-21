package com.evoucher.partner.service.vnpt.bean.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class VnptQueryBaseRequest {
    String requestId;
    String partnerName;
    String provider;

    String sign;
}
