package com.castis.publishservice.dto.queue;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BrandRequest {
    private String id;
    private String name;
    private String description;
    private String imgUrl;
    private SupplierRequest supplier;
    @JsonProperty("isPosLink")
    private boolean isPosLink;//0 co
    private String displayType;
    private String system;
}
