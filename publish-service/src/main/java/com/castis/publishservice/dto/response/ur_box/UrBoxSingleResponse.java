package com.castis.publishservice.dto.response.ur_box;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UrBoxSingleResponse<T> extends UrBoxResponse{
    private T data;
}
