package com.castis.publishservice.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VoucherGiftPopResponse {
    private String pinNo;
    private String trId;
    private String pinUrl;
    private String imgOrigin;
    private String imgCoupon;
    private String password;
}
