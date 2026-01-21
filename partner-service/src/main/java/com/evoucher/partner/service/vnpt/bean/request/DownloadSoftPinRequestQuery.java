package com.evoucher.partner.service.vnpt.bean.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class DownloadSoftPinRequestQuery extends VnptQueryBaseRequest {
    /**
     * face value
     */
    Integer amount;
    /**
     * total card
     */
    Integer quantity;
}
