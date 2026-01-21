package com.castis.publishservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublishDetailRequest {
    private Long publishDetailId;
    private String mobileNumber;
    private Long userId;
}
