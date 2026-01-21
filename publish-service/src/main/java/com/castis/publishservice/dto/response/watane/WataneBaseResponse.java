package com.castis.publishservice.dto.response.watane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WataneBaseResponse<T> {
    private Boolean success;
    private String code;
    private String message;
    private T data;
}
