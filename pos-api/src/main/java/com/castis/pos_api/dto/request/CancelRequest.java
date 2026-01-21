package com.castis.pos_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelRequest extends BaseRequest implements HavingTransactionId{
    String transactionId;

}
