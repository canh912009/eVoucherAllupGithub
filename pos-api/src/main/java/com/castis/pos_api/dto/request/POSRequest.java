package com.castis.pos_api.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class POSRequest {
    @Size(max = 4)
    private String posVerType;
    @Size(max = 2)
    private String posFuncType;
    @Size(max = 19)
    private String storeId;
    @Size(max = 10)
    private String posCd;
    @Size(max = 20)
    private String otp;
    private Double requestAmount;
    @Size(max = 20)
    private String approvementNo;
    private int numInputType;
}
