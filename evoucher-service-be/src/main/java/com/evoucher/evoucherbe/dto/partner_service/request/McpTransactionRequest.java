package com.evoucher.evoucherbe.dto.partner_service.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class McpTransactionRequest {
    String provider;
    String ev;
}
