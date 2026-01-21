package com.evoucher.adminapi.good.service.model.watane.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
