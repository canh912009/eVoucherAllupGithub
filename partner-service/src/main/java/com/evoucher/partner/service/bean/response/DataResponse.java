package com.evoucher.partner.service.bean.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DataResponse<T> extends BaseResponse {
    T data;

    public DataResponse (T data) {
        super();
        this.data = data;
    }

}
