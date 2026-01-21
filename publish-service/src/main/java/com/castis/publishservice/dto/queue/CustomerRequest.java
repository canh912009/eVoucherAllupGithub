package com.castis.publishservice.dto.queue;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerRequest {
    private String id;
    private String name;
}