package com.castis.publishservice.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VoucherResponse extends BaseResponse {
    private List<String> data;
    Long totalCount;
}
