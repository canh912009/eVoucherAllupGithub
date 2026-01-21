package com.castis.publishservice.dto.response.ur_box;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UrBoxResponse {
    private String done;
    private String msg;
    private String microtime;
    private Integer status;
    private Object gift;
}
