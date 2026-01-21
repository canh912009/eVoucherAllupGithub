package com.evoucher.adminapi.common.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBaseResponse {
    private Integer code;
    private String message;
    private String timestamp;
}
