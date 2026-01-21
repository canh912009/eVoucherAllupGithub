package com.evoucher.adminapi.good.service.model.watane.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Pagination {
    private int start;
    private int length;
}
