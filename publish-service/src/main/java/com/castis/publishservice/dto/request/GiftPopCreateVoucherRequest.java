package com.castis.publishservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftPopCreateVoucherRequest {
    private String authKey;
    private String goodsId;
    private String sendType;
    private String smsYN;
    @Builder.Default private String rcvPhoneNo = "";
    @Builder.Default private String sendTitle = "";
    @Builder.Default private String sendMsg = "";
    @Builder.Default private String sendLang = "";
    private int quantity;
    private String orderNo;
    @Builder.Default private String note = "";
}
