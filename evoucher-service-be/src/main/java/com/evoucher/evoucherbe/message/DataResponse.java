package com.evoucher.evoucherbe.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class DataResponse extends BaseResponse{
    private Object data;
    private Long totalCount;

    public DataResponse(Object data) {
        super();
        this.data = data;
    }
}
