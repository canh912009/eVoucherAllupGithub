package com.castis.publishservice.dto.request.watane;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AdditionalInfo {
    private String key;
    private String value;
}
