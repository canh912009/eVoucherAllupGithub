package com.castis.pos_api.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class ConfirmRequest {

    private String otp;
    private String storeId;
    private Double paymentAmount;
    private int posType;

    private String posCd;

    private String posVerType;

    private int otpInputType;

}
