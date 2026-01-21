package com.evoucher.partner.service.bean.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class VnptBaseRequest {
    String provider;
    String requestId;
}
