package com.evoucher.evoucherbe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShortUrlResponse {
    @JsonProperty(value = "ReqID")
    private String ReqID;
    @JsonProperty(value = "Code")
    private String Code;
    @JsonProperty(value = "Data")
    private String Data;
}
