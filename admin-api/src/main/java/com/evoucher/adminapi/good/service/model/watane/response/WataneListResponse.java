package com.evoucher.adminapi.good.service.model.watane.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class WataneListResponse<R> {
    private int total;
    private List<R> products;
}
