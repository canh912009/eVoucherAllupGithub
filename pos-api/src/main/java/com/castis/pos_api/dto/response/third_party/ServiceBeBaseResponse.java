package com.castis.pos_api.dto.response.third_party;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceBeBaseResponse {
    int code;
    String message;
    String timestamp;

    @JsonIgnore
    public boolean isOk() {
        return code == 0;
    }
}
