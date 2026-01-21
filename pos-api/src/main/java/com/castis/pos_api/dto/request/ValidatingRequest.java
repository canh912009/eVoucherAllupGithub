package com.castis.pos_api.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidatingRequest extends BaseRequest {
    @NotNull
    Integer keyType;
    @NotBlank
    String key;
    String password;
    String userPhoneNo;
}
