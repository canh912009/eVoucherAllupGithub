package com.castis.publishservice.dto.queue;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SupplierRequest {
    private String id;
    private String name;
    private String description;//
    private String imgUrl;//
}