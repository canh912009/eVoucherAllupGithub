package com.castis.pos_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FinalizeSingleRequest extends BaseRequest {
    @NotNull(message = "Key type is required")
    Integer keyType;
    @NotNull(message = "Key is required")
    String key;
    String password;
    @NotNull(message = "prepaid amount is required")
    @Positive(message = "prepaid amount must be positive")
    Double prepaidAmount;
}
