package com.castis.publishservice.dto.request;

import com.castis.publishservice.utils.status.EnumAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DataSynchronizeRequest<T> {
    private @NonNull EnumAction action;
    private @NonNull T payload;
}
