package com.castis.pos_api.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SupplierModel {
    private String id;
    private String name;
    private String description;
    private String imgUrl;
    private boolean isPosLink;
}
