package com.evoucher.adminapi.good.service.model.watane.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WataneProductDetailRequestBody implements WataneRequestBody {
    private String requestTime;
    private String code;
}
