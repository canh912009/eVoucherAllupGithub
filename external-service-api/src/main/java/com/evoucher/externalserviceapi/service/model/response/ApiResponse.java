package com.evoucher.externalserviceapi.service.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private String message;

    private String timestamp;

    private String errorCode;

    private Object data;

    private Long totalCount;
}
