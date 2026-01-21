package com.castis.pos_api.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthSuccessResponse extends ResponseData {
    private VoucherApiResponse posNetResVo;
    private String supplyComCd;
    private String productId;
    private String productType;
    private Double productCost;
    private Double balance;
    private String otpCode;
    private String validProductEndDate;
}
