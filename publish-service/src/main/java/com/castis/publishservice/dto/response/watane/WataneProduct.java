package com.castis.publishservice.dto.response.watane;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WataneProduct {
    private String code;
    private String name;
    private String url;
    private String validTo;
    private double value;
}
