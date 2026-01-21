package com.evoucher.partner.service.bean.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class VnptPurchaseRequest extends TransactionRequest {

}
