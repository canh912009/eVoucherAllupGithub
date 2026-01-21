package com.castis.publishservice.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class DataResponse extends BaseResponse{
    Object data;
    public DataResponse(Object data) {
        super();
        this.data = data;
    }
}
