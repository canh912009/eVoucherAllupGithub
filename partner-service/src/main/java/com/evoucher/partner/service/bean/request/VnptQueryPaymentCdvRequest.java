package com.evoucher.partner.service.bean.request;

import com.evoucher.partner.service.vnpt.bean.request.VnptQueryBaseRequest;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class VnptQueryPaymentCdvRequest extends VnptQueryBaseRequest {
    String account;
    Integer amount;
}
