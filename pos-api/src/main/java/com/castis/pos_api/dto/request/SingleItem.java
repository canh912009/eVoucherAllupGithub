package com.castis.pos_api.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;


@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SingleItem {
    @NotNull(message = "Key type is required")
    Integer keyType;
    @NotNull(message = "Key is required")
    String key;
    String password;
}
