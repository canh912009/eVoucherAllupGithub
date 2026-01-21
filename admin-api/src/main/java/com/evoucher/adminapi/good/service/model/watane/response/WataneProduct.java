package com.evoucher.adminapi.good.service.model.watane.response;

import lombok.Data;

@Data
public class WataneProduct {
    private String code;
    private String name;
    private int type;
    private double price;
    private double value;
    private int validIn; // months
    private String description;
    private boolean directVoucher;
    private int packageType;
}
