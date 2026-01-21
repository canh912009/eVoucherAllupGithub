package com.evoucher.externalserviceapi.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

    @Builder.Default
    private String message = "OK";

    @Builder.Default
    private String result = "0";

    private Object data;

    private Integer totalCount;

    public BaseResponse(Object data) {
        this.data = data;
    }
}
