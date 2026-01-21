package com.evoucher.adminapi.common.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiDataResponse<T> extends ApiBaseResponse {
    T data;
}
