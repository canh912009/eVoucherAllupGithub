package com.evoucher.adminapi.good.service.model.watane.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
