package com.evoucher.adminapi.good.service.model.ur_box;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UrBoxResponse {
    private String done;
    private String msg;
    private String microtime;
    private Integer status;
}
