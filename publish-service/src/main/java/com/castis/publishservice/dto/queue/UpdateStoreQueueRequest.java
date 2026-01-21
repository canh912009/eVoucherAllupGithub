package com.castis.publishservice.dto.queue;

import com.castis.publishservice.utils.status.EnumAction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateStoreQueueRequest {
    EnumAction action;
    StoreQueueRequest store;
}
