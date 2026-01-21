package com.castis.publishservice.dto.queue;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategoryRequest {
    private String id;
    private String name;
    private String description;//khong co
}