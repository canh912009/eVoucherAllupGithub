package com.castis.publishservice.dto.request.watane;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WataneRequestHeader {
    private String appId;
    private String signature;
    private String apiKey;
    private String token;
}
